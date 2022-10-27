import axios from "axios";

export default {
  namespaced: true,
  state: {
    notifications: [
      {
        icon: "post",
        name: "О новых публикациях",
        type: "POST",
        enable: false,
      },
      {
        icon: "comments",
        name: "О новых комментариях к моим публикациям",
        type: "POST_COMMENT",
        enable: false,
      },
      {
        icon: "reviews",
        name: "О ответах на мои комментарии",
        type: "COMMENT_COMMENT",
        enable: false,
      },
      {
        icon: "friends",
        name: "О заявках в друзья",
        type: "FRIEND_REQUEST",
        enable: false,
      },
      {
        icon: "messages",
        name: "О новых личных сообщениях",
        type: "MESSAGE",
        enable: false,
      },
      {
        icon: "birthdays",
        name: "О дне рождения друга",
        type: "FRIEND_BIRTHDAY",
        enable: false,
      },
      // {
      //   icon: "phone",
      //   name: "Отправлять уведомления на телефон",
      //   type: "SEND_PHONE_MESSAGE",
      //   enable: false,
      // },
      {
        icon: "email",
        name: "Отправлять уведомления на e-mail",
        type: "SEND_EMAIL_MESSAGE",
        enable: false,
      },
    ],
  },
  getters: {
    getNotificationsSettings: (s) => s.notifications,
  },
  mutations: {
    setNotificationsSettings(state, notifications) {
      let newSetting = [...state.notifications];
      newSetting.map((el) => {
        const foundElem = notifications.find((item) => {
          return item.notification_type === el.type;
        });
        el.enable = foundElem.enable;
        return el;
      });
      state.notifications = newSetting;
    },
  },
  actions: {
    passwordRecovery(context, email) {
      return axios({
        url: "auth/password/recovery",
        method: "POST",
        data: email,
      });
    },
    passwordSet(context, { password, temp }) {
      const data = {
        temp,
        password,
      };
      return axios({
        url: "auth/password/set",
        method: "PUT",
        data,
      });
    },
    async changeEmail(context, data) {
      await axios({
        url: "auth/email",
        method: "PUT",
        data: data,
      });
    },
    async changePassword(context, data) {
      await axios({
        url: "auth/password",
        method: "PUT",
        data: data,
      });
    },
    changeNotifications({ dispatch }, data) {
      return axios({
        url: "notifications/settings",
        method: "PUT",
        data,
      })
        .then(() => {
          dispatch(
            "global/alert/setAlert",
            {
              status: "success",
              text: "Настройки уведомления изменены",
            },
            {
              root: true,
            }
          );
        })
        .finally(() => {
          dispatch("apiNotificationsSettings");
        });
    },
    async apiNotificationsSettings({ commit }) {
      const response = await axios({
        url: "notifications/settings",
        method: "GET",
      });
      commit("setNotificationsSettings", response.data.data);
    },
  },
};
