import axios from "axios";
import moment from "moment";

export default {
  namespaced: true,
  state: {
    info: null,
  },
  getters: {
    getInfo(state) {
      if (!state.info) return;
      let result = {
        ...state.info,
      };
      result.fullName = result.first_name + " " + result.last_name;
      result.ages = moment().diff(result.birth_date, "years");
      return result;
    },
  },
  mutations: {
    setInfo: (s, info) => (s.info = info),
  },
  actions: {
    async apiInfo({ commit }) {
      await axios({
        url: "account/me",
        method: "GET",
      })
        .then(async (response) => {
          commit("setInfo", response.data.data);
        })
        .catch(() => {});
    },
    async apiChangeInfo({ commit, dispatch }, user) {
      await axios({
        url: "account/me",
        method: "PUT",
        data: user,
      })
        .then((response) => {
          dispatch(
            "global/alert/setAlert",
            {
              status: "success",
              text: "Информация обновлена",
            },
            {
              root: true,
            }
          );
          commit("setInfo", response.data.data);
        })
        .catch(() => {});
    },
    async deleteInfo() {
      await axios({
        url: "account/me",
        method: "DELETE",
      })
        .then(() => {})
        .catch(() => {});
    },
  },
};
