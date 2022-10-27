import axios from "axios";

export default {
  namespaced: true,
  state: {
    notifications: [],
    notificationsLength: 0,
  },
  getters: {
    getNotifications: (s) => s.notifications,
    getNotificationsLength: (s) => s.notificationsLength,
    getNotificationsTextType: () => (type) => {
      switch (type) {
        case "POST":
          return "опубликовал новую запись";
        case "POST_COMMENT":
          return "оставил комментарий";
        case "COMMENT_COMMENT":
          return "ответил на ваш комментарий";
        case "FRIEND_REQUEST":
          return "добавил в друзья нового пользователя";
        case "FRIEND_BIRTHDAY":
          return "день рождение";
        case "MESSAGE":
          return "прислал сообщение";
      }
    },
  },
  mutations: {
    setNotifications: (s, value) => (s.notifications = value),
    setNotificationsLength: (s, value) => (s.notificationsLength = value),
    addNotification(state, notification) {
      state.notifications.unshift(notification);
    },
    addNotificationsLength(state, notification) {
      if (notification.notification_type)
        state.notificationsLength = state.notificationsLength + 1;
    },
  },
  actions: {
    async fetchNotifications({ commit }) {
      try {
        const response = await axios({
          url: "notifications",
          method: "GET",
        });
        // for test
        // const response = {
        //   data: {
        //     data: [
        //       {
        //         author: {
        //           first_name: "Lilit-gmail",
        //           id: 14,
        //           last_name: "Oganisyan-gmail",
        //           photo: null,
        //         },
        //         content: `${Math.random(100)}`,
        //         notification_type: "POST",
        //         sent_time: 1655987588,
        //         id: Math.random(),
        //       },
        //     ],
        //   },
        // };
        console.log(response.data.data);
        commit("setNotifications", response.data.data);
      } catch (err) {
        // console.error({ err });
      }
    },
    async fetchNotificationsLength({ commit }) {
      try {
        const response = await axios({
          url: "notifications/count",
          method: "GET",
        });
        // for test
        // const response = { timestamp: 1656506484, data: { count: 1 } };
        commit("setNotificationsLength", response.data.data.count);
      } catch (err) {
        // console.error({ err });
      }
    },
  },
};
