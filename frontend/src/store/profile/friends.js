import axios from "axios";

export default {
  namespaced: true,
  state: {
    result: {
      friends: [],
      request: [],
      recommendations: [],
    },
  },
  getters: {
    getResult: (s) => s.result,
    getResultById: (s) => (id) => s.result[id],
  },
  mutations: {
    setResult: (s, payload) => (s.result[payload.id] = payload.value),
  },
  actions: {
    async apiFriends({ commit }, payload) {
      let query = [];
      payload &&
        Object.keys(payload).map((el) => {
          payload[el] && query.push(`${el}=${payload[el]}`);
        });
      await axios({
        url: `friends?${query.join("&")}`,
        method: "GET",
      })
        .then((response) => {
          commit("setResult", {
            id: "friends",
            value: response.data.data || [],
          });
        })
        .catch(() => {});
    },
    apiDeleteFriends({ dispatch }, id) {
      axios({
        url: `friends/${id}`,
        method: "DELETE",
      })
        .then((response) => {
          console.log("TCL: response", response);
          dispatch(
            "global/alert/setAlert",
            {
              status: "success",
              text: "Пользователь удален из друзей",
            },
            {
              root: true,
            }
          );
          dispatch("apiFriends");
        })
        .catch(() => {});
    },
    apiAddFriends({ dispatch, getters }, id) {
      console.log(getters.getResult.friends);
      let _friend = getters.getResult.friends.find((fr) => fr.id === id);
      if (_friend) console.log("apiAddFriends", id, _friend.statusCode);
      if (_friend && _friend.statusCode === "REQUEST_TO") {
        dispatch(
          "global/alert/setAlert",
          {
            status: "success",
            text: "Вы уже отправляли запрос этому пользователю!",
          },
          {
            root: true,
          }
        );
        return;
      }
      if (_friend && _friend.statusCode === "REJECTING") {
        dispatch(
          "global/alert/setAlert",
          {
            status: "success",
            text: "Этот пользователь заблокировал Вас!",
          },
          {
            root: true,
          }
        );
        return;
      }
      if (_friend && _friend.statusCode === "BLOCKED") {
        dispatch(
          "global/alert/setAlert",
          {
            status: "success",
            text: "Вы заблокировали этого пользователя!",
          },
          {
            root: true,
          }
        );
        return;
      }
      if (_friend && _friend.statusCode === "FRIEND") {
        dispatch(
          "global/alert/setAlert",
          {
            status: "success",
            text: "Вы уже друзья с этим пользователем!",
          },
          {
            root: true,
          }
        );
        return;
      }
      axios({
        url: `friends/${id}`,
        method: "POST",
      })
        .then((response) => {
          console.log("TCL: response", response);
          if (
            response.data.data &&
            response.data.data.message === "request already sent!"
          ) {
            dispatch(
              "global/alert/setAlert",
              {
                status: "success",
                text: "Вы уже отправляли запрос этому пользователю!",
              },
              {
                root: true,
              }
            );
          } else if (
            response.data.data &&
            response.data.data.message === "Blocked!"
          ) {
            dispatch(
              "global/alert/setAlert",
              {
                status: "success",
                text: "Этот пользователь заблокировал Вас!",
              },
              {
                root: true,
              }
            );
          } else
            dispatch(
              "global/alert/setAlert",
              {
                status: "success",
                text: "Заявка отправлена",
              },
              {
                root: true,
              }
            );
          dispatch("apiFriends");
        })
        .catch(() => {});
      dispatch(
        "global/search/searchUsers",
        "global/search/getLastSearchUsersRequest",
        {
          root: true,
        }
      );
    },
    apiSubscribe({ dispatch }, id) {
      axios({
        url: `friends/subscribe/${id}`,
        method: "POST",
      })
        .then(() => {
          dispatch(
            "global/alert/setAlert",
            {
              status: "success",
              text: "Заявка отправлена",
            },
            {
              root: true,
            }
          );
          dispatch("apiFriends");
          dispatch(
            "global/search/searchUsers",
            "global/search/getLastSearchUsersRequest",
            {
              root: true,
            }
          );
        })
        .catch(() => {});
    },
    async apiRequest({ commit }, payload) {
      let query = [];
      payload &&
        Object.keys(payload).map((el) => {
          payload[el] && query.push(`${el}=${payload[el]}`);
        });
      await axios({
        url: `friends/request?${query.join("&")}`,
        method: "GET",
      })
        .then((response) => {
          console.log("TCL: request", response);
          commit("setResult", {
            id: "request",
            value: response.data.data,
          });
        })
        .catch(() => {});
    },
    async apiRecommendations({ commit }, payload) {
      let query = [];
      payload &&
        Object.keys(payload).map((el) => {
          payload[el] && query.push(`${el}=${payload[el]}`);
        });
      await axios({
        url: `friends/recommendations?${query.join("&")}`,
        method: "GET",
      })
        .then((response) => {
          commit("setResult", {
            id: "recommendations",
            value: response.data.data || [],
          });
        })
        .catch(() => {});
    },
  },
};
