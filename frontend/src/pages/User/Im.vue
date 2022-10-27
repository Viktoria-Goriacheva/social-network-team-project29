<template>
  <div class="im">
    <div class="im__dialogs">
      <im-dialog
        v-for="dialog in dialogs"
        :key="dialog.id"
        :info="dialog"
        :push="countPush(dialog.unread_count)"
        :me="+info.id === +dialog.last_message.author_id"
        :active="+dialog.id === +activeDialogId"
        :online="checkOnlineUser(dialog)"
        @click.native="clickOnDialog(dialog.id)"
      ></im-dialog>
    </div>
    <div class="im__chat" v-if="activeDialog && messagesLoaded">
      <im-chat
        :info="activeDialog"
        :messages="messages"
        :online="checkOnlineUser(activeDialog)"
      />
    </div>
    <div v-else class="no-dialog">Диалог не выбран</div>
  </div>
</template>

<script>
import axios from "axios";
import { mapActions, mapState, mapMutations, mapGetters } from "vuex";
import ImDialog from "@/components/Im/Dialog";
import ImChat from "@/components/Im/Chat";

export default {
  name: "Im",
  components: { ImDialog, ImChat },
  props: {
    activeDialogId: {
      // type: String,
      required: false,
    },
  },
  data() {
    return {
      activeDialog: null,
      messagesLoaded: false,
    };
  },
  computed: {
    ...mapState("profile/dialogs", ["dialogs", "messages", "newMessage"]),
    ...mapState("profile/info", ["info"]),
  },
  methods: {
    ...mapActions("profile/dialogs", ["fetchDialogs", "fetchMessages"]),
    ...mapActions("users/info", ["userInfoId"]),
    ...mapMutations("profile/dialogs", [
      "addOneMessage",
      "setUnreadedMessages",
      "setNewMessage",
      "setActiveDialogId",
    ]),
    ...mapMutations("profile/dialogs", ["setDialogs"]),
    ...mapGetters("users/info", ["getUsersInfo"]),
    ...mapGetters("profile/dialogs", ["getDialogs"]),
    // ...mapGetters("profile/info", ["getInfo"]),

    generateNewDialog(user) {
      return {
        id: this.activeDialogId,
        conversationPartner: {
          id: this.activeDialogId,
          first_name: user.first_name,
          last_name: user.last_name,
          last_online_time: user.last_online_time,
          is_online: user.is_online,
          photo: user.photo || null,
        },
        last_message: {},
        unread_count: 0,
      };
    },

    countPush(unread) {
      return unread > 0 ? unread : null;
    },
    checkOnlineUser(dialog) {
      return dialog.conversationPartner.is_online;
    },
    clickOnDialog(dialogId) {
      if (+dialogId === +this.activeDialogId) {
        return;
      }
      this.$router.push({
        name: "ImChat",
        params: { activeDialogId: dialogId.toString() },
      });
    },
  },
  watch: {
    activeDialogId: {
      immediate: true,
      async handler(value) {
        await this.fetchDialogs();
        if (value) {
          this.messagesLoaded = false;
          await this.fetchMessages(value);
          this.messagesLoaded = true;

          this.messagesLoaded = true;
          const newActiveDialog = this.getDialogs().filter(
            (d) => +d.id === +value
          );
          if (newActiveDialog.length > 0) {
            this.activeDialog = newActiveDialog[0];
            this.activeDialog = newActiveDialog[0];
            this.activeDialog.unread_count = 0;
          } else {
            await this.userInfoId(+this.activeDialogId).then(() => {
              this.activeUser = this.getUsersInfo();
              this.activeDialog = this.generateNewDialog(this.activeUser);
              this.setDialogs([...this.dialogs, this.activeDialog]);
            });
          }
        }
      },
    },
    newMessage: {
      handler(message) {
        // new message of partner
        const interlocutor_id = message.author_id;
        const message_id = message.id;
        if (
          this.activeDialogId &&
          +message.author_id === +this.activeDialogId
        ) {
          message.isSentByMe = false;
          this.addOneMessage(message);
          axios.put(`/dialogs/${interlocutor_id}/messages/${message_id}/read`);
        }
      },
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl';

.no-dialog {
  display: flex;
  width: 100%;
  height: 100%;
  justify-content:center;
  align-items: center;
  color: #666;
}

.im {
  display: flex;
  height: 'calc(100vh - %s)' % header-height;
}

.im__dialogs {
  width: 100%;
  max-width: 39.13%;
  overflow-y: auto;
  max-height: 100%;
  height: 100%;
  background-color: #eaf0fb;

  @media (max-width: breakpoint-xl) {
    max-width: 120px;
  }
}

.im__chat {
  width: 100%;
  flex: auto;
  height: 100%;
  background-color: #F8FAFD;
}
</style>
