import axios from "axios";
import moment from "moment";

export default {
  namespaced: true,
  state: {
    info: null,
    wall: [],
    users: null,
  },
  getters: {
    getInfo(state) {
      if (!state.info) return;
      let result = {
        ...state.info,
      };
      // если понадобиться то добавить склонение (для публикаций, но нужен или пол или отчество)
      // библиотека - petrovich
      result.fullName = result.first_name + " " + result.last_name;
      result.ages = moment().diff(result.birth_date, "years");
      // result.is_onlined =
      //   moment().diff(moment(result.last_online_time), "seconds") <= 60;
      return result;
    },
    getUsersInfo(state) {
      if (!state.users) return;
      let result = {
        ...state.users,
      };
      // если понадобиться то добавить склонение (для публикаций, но нужен или пол или отчество)
      // библиотека - petrovich
      result.fullName = result.first_name + " " + result.last_name;
      result.ages = moment().diff(result.birth_date, "years");
      // result.is_onlined =
      //   moment().diff(moment(result.last_online_time), "seconds") <= 60;
      return result;
    },
    getWall: (s) => s.wall,
    getWallPostedLength: (s) =>
      s.wall.filter((el) => el.type === "POSTED").length,
    getWallQueuedLength: (s) =>
      s.wall.filter((el) => el.type === "QUEUED").length,
  },
  mutations: {
    setInfo: (s, info) => {
      // console.log("setInfo", info);
      s.info = info;
    },
    setWall(s, wall) {
      s.wall = wall;
    },
    setWallById: (s, payload) =>
      (s.wall[s.wall.indexOf(s.wall.find((el) => el.id === payload.id))] =
        payload.value),
    setCommentsById: (s, payload) => {
      s.wall[
        s.wall.indexOf(s.wall.find((el) => el.id === payload.post_id))
      ].comments = payload.value;
      s.wall.push("dog-nail");
      s.wall.splice(-1, 1);
    },
    setUsersInfo: (s, info) => (s.users = info),
  },
  actions: {
    async apiInfo({ commit }) {
      const response = await axios({
        url: `account/me`,
        method: "GET",
      });
      commit("setInfo", response.data.data);
      commit("setWall", response.data.data.posts);
    },

    async apiWall({ commit }, { page, itemPerPage }) {
      const response = await axios({
        url: `post/wall${page ? "?page=" + page : ""}${
          itemPerPage ? "&itemPerPage=" + itemPerPage : ""
        }`,
        method: "GET",
      });
      if (response.data.data) commit("setWall", response.data.data);
    },
    async apiProfileWall({ commit }, { id, page, itemPerPage }) {
      const response = await axios({
        url: `post/wall/${id}${page ? "?page=" + page : ""}${
          itemPerPage ? "&itemPerPage=" + itemPerPage : ""
        }`,
        method: "GET",
      });
      if (response.data.data) commit("setWall", response.data.data);
    },
    async apiWallById({ commit }, id) {
      await axios({
        url: `post/${id}`,
        method: "GET",
      })
        .then((response) => {
          commit("setWallById", {
            id,
            value: response.data.data,
          });
        })
        .catch(() => {});
    },
    async apiCommentsById({ commit }, id) {
      await axios({
        url: `post/${id}/comments`,
        method: "GET",
      })
        .then((response) => {
          commit("setCommentsById", response.data.data);
        })
        .catch(() => {});
    },
    async userInfoId({ commit, dispatch }, id) {
      await axios({
        url: `account/${id}`,
        method: "GET",
      })
        .then(async (response) => {
          commit("setUsersInfo", response.data);
          await dispatch("apiProfileWall", { id });
        })
        .catch(() => {});
    },
  },
};
