import axios from "axios";

export default {
  namespaced: true,
  state: {
    captcha: { secret: "", imgCode: "" },
  },
  getters: {
    getCaptcha: (state) => {
      return state.captcha;
    },
  },
  mutations: {
    setCaptcha: (state, value) => {
      state.captcha = value;
    },
  },
  actions: {
    async fetchCaptcha({ commit }) {
      await axios({
        url: "/auth/captcha",
        method: "GET",
      })
        .then((response) => {
          commit("setCaptcha", {
            imgCode: response.data.image,
            secret: response.data.secret,
          });
        })
        .catch(() => {});
    },
  },
};
