<template lang="pug">
  .push-block
    .push__img(v-if="info.author.photo")
      img(:src="info.author.photo" :alt="info.author.first_name[0]")
    .push__img(v-else)
      div {{ info.author.first_name[0] + " " + info.author.last_name[0] }}
    p.push__content
      router-link.push__content-name(:to="getRouteByNotification(info)")
        | {{info.author.first_name + " " + info.author.last_name}}
        |
        | {{getNotificationsTextType(info.notification_type)}}
      span.push__content-preview  «{{info.content}}»
    span.push__time {{info.sent_time | moment('from')}}
</template>

<script>
import { mapGetters } from "vuex";
import { getRouteByNotification } from "@/utils/notifications.utils.js";
export default {
  name: "PushBlock",
  props: {
    info: Object,
  },
  computed: {
    ...mapGetters("profile/notifications", ["getNotificationsTextType"]),
  },
  methods: {
    getRouteByNotification,
  },
};
</script>

<style lang="stylus">
.push-block {
  background: #fff;
  padding: 25px 30px;
  box-shadow: 0px 2px 60px rgba(0, 0, 0, 0.1);
  display: flex;
  border-radius: 3%;
  &+& {
    margin-top: 20px;
  }

  .push__img {
    flex: none;
  }

  .push__content {
    max-width: 650px;
    padding-top: 10px;
    margin-right: 30px;
  }

  .push__time {
    flex: none;
    padding-top: 10px;
  }
}

.push__img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
  flex: none;
  background-color: #e6f4eb;

  img {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  div {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}
</style>
