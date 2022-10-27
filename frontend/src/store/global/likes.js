import axios from "axios";
import router from "@/router";

export default {
  namespaced: true,
  actions: {
    async putLike({ dispatch }, data) {
      await axios({
        url: `likes`,
        method: "PUT",
        data,
      })
        .then(() => {
          dispatch("likeAction", data);
        })
        .catch(() => {});
    },
    async deleteLike({ dispatch }, data) {
      await axios({
        url: `likes`,
        method: "DELETE",
        data,
      })
        .then(() => {
          dispatch("likeAction", data);
        })
        .catch(() => {});
    },
    async likeAction({ dispatch }, data) {
      if (data.type === "POST") {
        router.history.current.name === "News"
          ? dispatch("profile/feeds/apiFeedsById", data.item_id, {
              root: true,
            })
          : dispatch("users/info/apiWallById", data.item_id, {
              root: true,
            });
      } else {
        dispatch("profile/comments/commentsById", data.post_id, {
          root: true,
        });
      }
    },
  },
};
