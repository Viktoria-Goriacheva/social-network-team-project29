import axios from "axios";

// const mergeIncomingMessages = ({ commit, state }, response) => {
//   const fromServerNewFirst = response.data.data;
//   fromServerNewFirst.forEach((m) => (m.sid = "" + m.id));
//   const onlyNewMessages = fromServerNewFirst.filter(
//     (msgServer) => !state.messages.some((m) => m.id === msgServer.id)
//   );
//   if (onlyNewMessages.length > 0) {
//     commit("addMessages", {
//       messages: onlyNewMessages,
//       total: response.data.total,
//     });
//   }
// };

//  [msg 0] [msg 1] .... [msg 10]
//                          ^
//                          oldestLoadedId
// <-newest messages------------------------------------------------------older
export default {
  namespaced: true,
  state: {
    dialogs: [],
    unreadedMessages: 0, // total unreaded
    messages: [], // sorted oldest->newest 0 is oldest
    totalMessages: null,
    dialogsLoaded: false,
    activeDialogId: null,
    oldLastKnownMessageId: null,
    isHistoryEndReached: false,
    newMessage: {},
  },
  getters: {
    getDialogs: (s) => s.dialogs,
    getMessages: (s) => s.messages,
    oldestKnownMessageId: (s) =>
      s.messages.length > 0 ? s.messages[0]["id"] : null,
    activeDialog: (s) => s.dialogs.find((el) => el.id == s.activeDialogId),
    getActiveDialogId: (s) => +s.activeId,
    dialogsLoaded: (s) => s.dialogsLoaded,
    unreadedMessages: (s) => s.unreadedMessages,
    hasOpenedDialog: (s) => (dialogId) =>
      !!s.dialogs.find((el) => el.id == dialogId),
    isHistoryEndReached: (s) => s.isHistoryEndReached,
  },
  mutations: {
    clearMessages(state) {
      state.messages = [];
    },
    setUnreadedMessages: (s, unread) => (s.unreadedMessages = unread),
    setDialogs: (s, dialogs) => (s.dialogs = dialogs),
    setNewMessage: (s, newMessage) => (s.newMessage = newMessage),
    dialogsLoaded: (s) => (s.dialogsLoaded = true),
    setActiveDialogId: (s, value) => (s.activeDialogId = value),
    addMessages: (s, { messages, total }) => {
      s.messages = [...s.messages, ...messages];
      s.messages.sort((a, b) => a.time - b.time);
      s.total = total;
    },
    addOneMessage: (state, messagePayload) => {
      console.log("addOneMessage", messagePayload);
      state.messages.push(messagePayload);
    },
    selectDialog: (state, dialogId) => {
      state.activeId = dialogId;
      state.messages = [];
      state.isHistoryEndReached = false;
    },
    markEndOfHistory: (s) => (s.isHistoryEndReached = true),
  },
  actions: {
    async fetchMessages({ commit }, dialogId) {
      console.log("fetchMessages");
      const response = await axios.get(
        `/dialogs/messages?interlocutorId=${dialogId}`
      );
      ////////test code
      console.log(dialogId);
      // const response = {
      //   data: {
      //     timestamp: 1652277651,
      //     total: 3,
      //     data: [
      //       {
      //         time: 65454545,
      //         author_id: 15,
      //         id: 1,
      //         message_text: "text1",
      //       },
      //       {
      //         time: 65454545,
      //         author_id: 13,
      //         id: 2,
      //         message_text: "text2",
      //       },
      //       {
      //         time: 65454545,
      //         author_id: 15,
      //         id: 3,
      //         message_text: "text3",
      //       },
      //     ],
      //   },
      // };
      commit("clearMessages");
      commit("addMessages", {
        messages: response.data.data,
        total: response.data.total,
      });
    },
    async fetchDialogs({ commit }) {
      try {
        const response = await axios.get("/dialogs");
        if (response.data?.data?.length === 0) {
          return;
        }
        // const response = {
        //   data: {
        //     timestamp: 1652277651,
        //     current_user_id: "13",
        //     data: [
        //       {
        //         unread_count: 5,
        //         last_message: {
        //           author: {
        //             id: "15",
        //             photo:
        //               "http://res.cloudinary.com/dhtldn8id/image/upload/v1653641996/diploma_alpha/de8f32028a60a25c59b6d017c3450c12.jpg",
        //             first_name: "Lilit-gmail",
        //             last_name: "Gribayedova",
        //             last_online_time: "12324656545987878484",
        //           },
        //           time: 1655472575,
        //           message_text: "some text",
        //           status: "online",
        //           recipient_id: 13,
        //         },
        //         conversationPartner: {
        //           photo:
        //             "http://res.cloudinary.com/dhtldn8id/image/upload/v1653641996/diploma_alpha/de8f32028a60a25c59b6d017c3450c12.jpg",
        //           id: 15,
        //           first_name: "Lilit-gmail",
        //           last_name: "Gribayedova",
        //           last_online_time: "12324656545987878484",
        //           is_online: false,
        //         },
        //       },
        //       {
        //         unread_count: 0,
        //         last_message: {
        //           author_id: 9,
        //           time: 1655472579,
        //           message_text: "some text",
        //           status: "online",
        //           recipient_id: 13,
        //         },
        //         conversationPartner: {
        //           photo:
        //             "http://res.cloudinary.com/dhtldn8id/image/upload/v1653641996/diploma_alpha/de8f32028a60a25c59b6d017c3450c12.jpg",
        //           id: 9,
        //           first_name: "Lilit-gmail",
        //           last_name: "Gribayedova",
        //           last_online_time: "12324656545987878484",
        //           is_online: false,
        //         },
        //       },
        //     ],
        //   },
        // };
        // re-map message
        const dialogs = [];
        // response.data.data.forEach((d) => {
        //   const conversationPartnerId =
        //     d.last_message.author.id === response.data.current_user_id
        //       ? d.last_message.recipient_id
        //       : d.last_message.author.id;
        //   const newDialog = {
        //     id: conversationPartnerId,
        //     unread_count: d.unread_count,
        //     last_message: {
        //       time: d.last_message.time,
        //       message_text: d.last_message.message_text,
        //       status: d.last_message.status,
        //       author_id: d.last_message.author.id,
        //     },
        //     conversationPartner: {
        //       photo: d.last_message.author.photo,
        //       id: conversationPartnerId,
        //       first_name: d.last_message.author.first_name,
        //       last_name: d.last_message.author.last_name,
        //       last_online_time: d.last_message.author.last_online_time,
        //       is_online: false,
        //     },
        //   };
        //   dialogs.push(newDialog);
        // });
        response.data.data.forEach((d) => {
          const conversationPartnerId = d.conversationPartner.id;
          const newDialog = {
            id: conversationPartnerId,
            unread_count: d.unread_count,
            last_message: {
              time: d.last_message.time,
              message_text: d.last_message.message_text,
              author_id: d.last_message.author_id,
            },
            conversationPartner: d.conversationPartner,
          };
          dialogs.push(newDialog);
        });
        console.log({ dialogs });
        commit("setDialogs", dialogs);
      } catch (err) {
        console.log({ err });
      }
    },
  },
};
