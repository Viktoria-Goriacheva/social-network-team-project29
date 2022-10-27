import axios from "axios";
import router from "@/router";
import Vue from "vue";

export default {
  namespaced: true,
  state: {
    lastSearchUsersRequest: "",
    lastSearchNewsRequest: "",
    usersQueryParams: {},
    newsQueryParams: {},
    searchText: null,
    usersPagination: { total: 20, page: 1, size: 5 },
    newsPagination: { total: 20, page: 1, size: 5 },
    tabs: [
      // {
      //   id: "all",
      //   text: "Всё",
      // },
      {
        id: "users",
        text: "Люди",
      },
      {
        id: "news",
        text: "Новости",
      },
    ],
    tabSelect: "users",
    result: {
      users: [],
      news: [],
    },
    status: "",
  },
  getters: {
    searchText: (s) => s.searchText,
    tabs: (s) => s.tabs,
    tabSelect: (s) => s.tabSelect,
    getResult: (s) => s.result,
    getResultById: (s) => (id) => s.result[id],
    getStatus: (s) => s.status,
    getLastSearchUsersRequest: (s) => s.lastSearchUsersRequest,
    getLastSearchNewsRequest: (s) => s.lastSearchNewsRequest,
    getUsersQueryParams: (s) => s.usersQueryParams,
    getNewsQueryParams: (s) => s.newsQueryParams,
    getUsersPagination: (s) => s.usersPagination,
    getNewsPagination: (s) => s.newsPagination,
  },
  mutations: {
    setUsersPagination: (s, pagination) => (s.usersPagination = pagination),
    setNewsPagination: (s, pagination) => (s.newsPagination = pagination),
    setSearchText: (s, value) => (s.searchText = value),
    setLastSearchUsersRequest: (s, value) => (s.lastSearchUsersRequest = value),
    setLastSearchNewsRequest: (s, value) => (s.lastSearchNewsRequest = value),
    setUsersQueryParams: (s, value) => (s.usersQueryParams = value),
    setNewsQueryParams: (s, value) => (s.newsQueryParams = value),
    setTabSelect: (s, id) => (s.tabSelect = id),
    routePushWithQuery(state, id) {
      let query = {};
      if (id !== "all") query.tab = id;
      if (state.searchText) query.text = state.searchText;
      router.push({
        name: "Search",
        query,
      });
    },
    setResult(s, result) {
      s.result[result.id] = result.value;
      Vue.set(s.result, result.id, result.value);
    },
  },
  actions: {
    clearSearch({ commit }) {
      commit("setSearchText", "");
      commit("setResult", {
        id: "users",
        value: [],
      });
      commit("setResult", {
        id: "news",
        value: [],
      });
    },
    changeTab({ commit }, id) {
      commit("setTabSelect", id);
      commit("routePushWithQuery", id);
    },
    async searchUsers({ commit }, payload) {
      let query = [];
      payload &&
        Object.keys(payload).map((el) => {
          if (payload[el] !== "undefined" && payload[el] !== null)
            query.push(`${el}=${payload[el]}`);
        });
      await axios({
        url: `account/search?${query.join("&")}`,
        method: "GET",
      })
        .then((response) => {
          commit("setResult", {
            id: "users",
            value: response.data.data,
          });
          commit("setUsersQueryParams", payload);
          commit(
            "setLastSearchUsersRequest",
            `account/search?${query.join("&")}`
          );
          let pagination = {};
          pagination.total = response.data.total;
          pagination.size = response.data.size;
          pagination.page = response.data.page + 1;
          commit("setUsersPagination", pagination);
        })
        .catch(() => {});
    },
    async searchNews({ commit }, payload) {
      let query = [];
      payload &&
        Object.keys(payload).map((el) => {
          if (payload[el] !== "undefined" && payload[el] !== null)
            query.push(`${el}=${payload[el]}`);
        });
      await axios({
        url: `post?${query.join("&")}`,
        method: "GET",
      })
        .then((response) => {
          let result = response.data.data.filter(
            (feed) => feed.author !== undefined
          );
          commit("setResult", {
            id: "news",
            value: result,
          });
          commit("setNewsQueryParams", payload);
          commit("setLastSearchNewsRequest", `post?${query.join("&")}`);
          let pagination = {};
          pagination.total = response.data.total;
          pagination.size = response.data.size;
          pagination.page = response.data.page + 1;
          commit("setNewsPagination", pagination);
        })
        .catch(() => {});
    },
    async searchAll({ dispatch, state }, text) {
      // commit("setSearchText", text);
      let searchQueryUsers = Object.assign({}, state.usersQueryParams, {
        author: text,
        size: state.usersPagination.size,
        page: state.usersPagination.page - 1,
      });
      await dispatch("searchUsers", searchQueryUsers);
      let searchQueryNews = Object.assign({}, state.newsQueryParams, {
        text: text ? text : "",
        size: state.newsPagination.size,
        page: state.newsPagination.page - 1,
      });
      await dispatch("searchNews", searchQueryNews);
    },
  },
};
