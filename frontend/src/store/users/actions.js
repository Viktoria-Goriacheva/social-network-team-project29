import axios from "axios";

export default {
  namespaced: true,
  actions: {
    async apiBlockUser({ dispatch }, id) {
      await axios({
        url: `friends/block/${id}`,
        method: "PUT",
      })
        .then(() => {
          dispatch(
            "global/alert/setAlert",
            {
              status: "success",
              text: "Пользователь заблокирован",
            },
            {
              root: true,
            }
          );
          dispatch("profile/friends/apiFriends", null, {
            root: true,
          });
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
    async apiUnblockUser({ dispatch }, id) {
      await axios({
        url: `friends/block/${id}`,
        method: "PUT",
      })
        .then((response) => {
          dispatch(
            "global/alert/setAlert",
            {
              status: "success",
              text: "Пользователь разблокирован",
            },
            {
              root: true,
            }
          );
          dispatch("profile/friends/apiFriends", null, {
            root: true,
          });
          dispatch(
            "global/search/searchUsers",
            "global/search/getLastSearchUsersRequest",
            {
              root: true,
            }
          );
          console.log("TCL: apiUnblockUser -> response", response);
        })
        .catch(() => {});
    },
  },
};
