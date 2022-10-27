// import router from "@/router";
// import store from "@/store";

export default {
  install(Vue, options) {
    let socket;

    Vue.prototype.$socket = {
      // connect: function (token) {
      connect: function connection(token) {
        // if (socket) {
        //   socket.close();
        // }
        const serverUrl = "ws://" + options.server + "/api/v1/streaming/ws";
        socket = new WebSocket(serverUrl);
        console.log("function connect");
        socket.onopen = () => {
          console.log("Websocket connected...");
          const connectObject = {
            token,
            type: "Connect",
            text: "Hello, server!",
          };
          let connectMessage = JSON.stringify(connectObject);
          socket.send(connectMessage);
        };

        socket.onmessage = (event) => {
          let serverMessage = { type: "none" };
          let msg = event.data;
          // let msg = JSON.stringify(event.data);

          try {
            serverMessage = JSON.parse(msg);
            //somthing strange, it throws error but parses to json
            console.log(serverMessage);
          } catch (e) {
            // console.log({ e });
          }
          //  { type: "online", status: true, userId: "" }
          // real time online decision
          // if (serverMessage.type === "ONLINE") {
          //   const currentRoute = router.history.current;
          //   // console.log(router.history.current);
          //   const routeName = currentRoute.name;
          //   console.log(serverMessage.userId, currentRoute.params.id);
          //   if (
          //     routeName === "ProfileId" &&
          //     serverMessage.userId === currentRoute.params.id
          //   ) {
          //     // update profile online status profileId
          //     let newInfo = {};
          //     let userInfo = store.getters["users/info/getUsersInfo"];
          //     console.log(userInfo, userInfo.is_onlined, serverMessage.status);
          //     if (userInfo && userInfo.id) {
          //       newInfo = Object.assign({}, userInfo, {
          //         is_onlined: serverMessage.status,
          //       });
          //       console.log(newInfo, store.commit);
          //       store.commit("users/info/setUsersInfo", newInfo);
          //     }
          //
          //     // console.log(store.getters["users/info/getInfo"]);
          //   }
          //   if (routeName === "Im") {
          //     console.log("update profile online status in messages");
          //   }
          // }
        };
        socket.onerror = (e) => {
          // Сервер недоступен
          console.log("connect error!!", { e });
          return;
        };

        socket.onclose = (e) => {
          // Сервер недоступен
          console.log("connecttion closed!!", { e });
          return;
        };
      },
      sendMessage: function (payload) {
        const message = JSON.stringify(payload);
        console.log(message);
        socket.send(message);
      },
      subscribe: function (eventType, callback) {
        console.log(eventType);
        //for testing
        // setInterval(() => {
        //   let messagePayload = {
        //     type: "NOTIFICATION",
        //     data: {
        //       author: {
        //         first_name: "Lilit-gmail",
        //         id: 14,
        //         last_name: "Oganisyan-gmail",
        //         photo: null,
        //       },
        //       content: `${Math.random(100)}`,
        //       notification_type: "POST",
        //       timestamp: 1655987588,
        //       id: Math.random(),
        //     },
        //   };
        //   callback(messagePayload);
        // }, 10000);
        // имитация сокетов для теста
        // setInterval(() => {
        //   let messagePayload = {
        //     type: "MESSAGE",
        //     data: {
        //       time: 65454545,
        //       author_id: 14,
        //       id: Math.random(10),
        //       message_text: `${Math.random(100)}`,
        //       recipient_id: 13,
        //     },
        //   };
        //   callback(messagePayload);
        // }, 10000);
        socket.onmessage = (event) => {
          let serverMessage = null;
          let msg = event.data;
          try {
            serverMessage = JSON.parse(msg);
            //somthing strange, it throws error but parses to json
            console.log(serverMessage);
          } catch (e) {
            // console.log({ e });
          }
          callback(serverMessage);
        };
      },
    };
  },
};
