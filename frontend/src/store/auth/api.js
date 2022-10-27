import axios from "axios";

export default {
  namespaced: true,
  state: {
    token: localStorage.getItem("user-token") || "",
    status: "",
    pollingToken: null,
    refreshAttempts: 0,
    isCode: true,
  },
  getters: {
    apiToken: (s) => s.token,
    isAuthenticated: (state) => !!state.token,
    authStatus: (state) => state.status,
    getIsCode: (state) => state.isCode,
  },
  mutations: {
    setToken: (s, token) => (s.token = token),
    setIsCode: (s, isCode) => (s.isCode = isCode),
    setStatus: (s, status) => (s.status = status),
    addAttempts: (s) => (s.refreshAttempts = s.refreshAttempts + 1),
    resetAttempts: (s) => (s.resetAttmpts = 0),
    setPollingInterval(state, interval) {
      state.pollingToken = interval;
    },
  },
  actions: {
    async register({ dispatch }, user) {
      await axios({
        url: "auth/register",
        data: user,
        method: "POST",
      }).then(async () => {
        dispatch(
          "global/alert/setAlert",
          {
            status: "success",
            text: "Зарегистрирован, делаю логин",
          },
          {
            root: true,
          }
        );
        await dispatch("login", {
          email: user.email,
          password: user.passwd1,
        });
      });
    },
    // pollingToken({ commit, dispatch }) {
    //   const interval = setInterval(() => {
    //     dispatch("refreshToken");
    //   }, 1000 * 600 * 1.5);
    //   commit("setPollingInterval", interval);
    // },
    refreshToken({ commit, state, dispatch }) {
      if (state.refreshAttempts > 3) {
        console.warn(
          "Cannot refresh token. Logging out...",
          state.pollingToken
        );
        dispatch("logout");
        clearInterval(state.pollingToken);
        return;
      }
      const refreshToken = localStorage.getItem("refresh-token");
      axios
        .post("auth/refresh", { refreshToken })
        .then((res) => {
          const newAccessToken = res.data.data.accessToken;
          const newRefreshToken = res.data.data.refreshToken;
          localStorage.setItem("user-token", newAccessToken);
          localStorage.setItem("refresh-token", newRefreshToken);
          document.cookie = `jwt=${newAccessToken}`;
          axios.defaults.headers.common[
            "Authorization"
          ] = `Bearer ${newAccessToken}`;
          commit("resetAttempts");
        })
        .catch(() => {
          console.warn("Cannot get new access token", state.refreshAttempts);
          commit("addAttempts");
        });
    },
    login({ commit, dispatch }, user) {
      commit("setStatus", "loading");
      return new Promise((resolve, reject) => {
        axios({ url: "auth/login", data: user, method: "POST" })
          .then((response) => {
            const token = response.data.data.token;
            const refreshToken = response.data.data.refreshToken;
            localStorage.setItem("user-token", token);
            localStorage.setItem("refresh-token", refreshToken);
            document.cookie = `jwt=${token}`;
            axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
            commit("setToken", token);
            commit("setStatus", "success");
            commit("profile/info/setInfo", response.data.data, {
              root: true,
            });
            dispatch("pollingToken");
            resolve();
          })
          .catch((error) => {
            commit("setStatus", "error");
            localStorage.removeItem("user-token");
            localStorage.removeItem("refresh-token");
            document.cookie = "jwt=";
            reject(Error(error));
          });
      });
    },
    async logout({ commit, dispatch, state }) {
      await axios({ url: "auth/logout", method: "POST" });
      commit("setToken", "");
      commit("setStatus", "logout");
      dispatch(
        "global/alert/setAlert",
        { status: "success", text: "Вы вышли из системы" },
        { root: true }
      );
      localStorage.removeItem("user-token");
      localStorage.removeItem("refresh-token");
      document.cookie = "jwt=";
      clearInterval(state.pollingToken);
      delete axios.defaults.headers.common["Authorization"];
    },
  },
};
