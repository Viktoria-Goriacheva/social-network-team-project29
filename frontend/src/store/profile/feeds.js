import axios from "axios";

export default {
  namespaced: true,
  state: {
    feeds: [],
    feedsPagination: { total: 20, page: 1, size: 5 },
  },
  getters: {
    getFeeds: (s) => s.feeds,
    getFeedsPagination: (s) => s.feedsPagination,
  },
  mutations: {
    setFeeds: (s, feeds) => (s.feeds = feeds),
    setFeedsPagination: (s, pagination) => (s.feedsPagination = pagination),
    setCommentsById: (s, payload) => {
      s.feeds[
        s.feeds.indexOf(s.feeds.find((el) => el.id === payload.post_id))
      ].comments = payload.value;
      s.feeds.push("dog-nail");
      s.feeds.splice(-1, 1);
    },
    setFeedsById: (s, payload) =>
      (s.feeds[s.feeds.indexOf(s.feeds.find((el) => el.id === payload.id))] =
        payload),
  },

  actions: {
    async apiFeeds({ commit }, payload) {
      let query = [];
      payload &&
        Object.keys(payload).map((el) => {
          if (payload[el] !== "undefined" && payload[el] !== null)
            query.push(`${el}=${payload[el]}`);
        });
      await axios({
        url: `feeds?${query.join("&")}`,
        method: "GET",
      })
        .then((response) => {
          commit("setFeeds", response.data.data);
          let pagination = {};
          pagination.total = response.data.total;
          pagination.size = response.data.size;
          pagination.page = response.data.page + 1;
          commit("setFeedsPagination", pagination);
        })
        .catch(() => {});
    },
    async apiFeedsById({ commit }, post_id) {
      await axios({
        url: `post/${post_id}`,
        method: "GET",
      })
        .then((response) => {
          commit("setFeedsById", response.data);
        })
        .catch(() => {});
    },
    async actionsFeed({ dispatch }, payload) {
      let url = payload.edit ? `post/${payload.post_id}` : `post`;
      let method = payload.edit ? "PUT" : "POST";
      if (payload.publish_date) url += "?publish_date=" + payload.publish_date;
      await axios({
        url,
        method,
        data: {
          title: payload.title,
          post_text: payload.post_text,
          tags: payload.tags,
          photo_url: payload.photo_url,
        },
      })
        .then(() => {
          if (payload.edit) {
            dispatch("users/info/apiWallById", payload.post_id, {
              root: true,
            });
          }
          if (payload.route === "News") {
            dispatch("apiFeeds");
          } else
            dispatch(
              "users/info/apiWall",
              {
                id: payload.id,
              },
              {
                root: true,
              }
            );
        })
        .catch(() => {});
    },
    async deleteFeeds({ dispatch }, payload) {
      await axios({
        url: `post/${payload.post_id}`,
        method: "DELETE",
      })
        .then(() => {
          payload.route === "News"
            ? dispatch("apiFeeds")
            : dispatch(
                "users/info/apiWall",
                {
                  id: payload.id,
                },
                {
                  root: true,
                }
              );
        })
        .catch(() => {});
    },
  },
};
