<template>
  <div class="push" :class="{ open: isOpen }">
    <div class="push__overlay" @click.stop="closePush"></div>
    <div class="push__wrap" :class="{ open: isOpen }" ref="wrap">
      <div class="push__list" ref="list">
        <div class="push__item" v-for="info in getNotifications" :key="info.id">
          <div class="main-layout__user-pic" style="background-color: #8bc49e">
            <div class="push__img" v-if="info.author.photo">
              <img :src="info.author.photo" :alt="info.author.first_name" />
            </div>
            <div v-else>
              {{ info.author.first_name[0] + " " + info.author.last_name[0] }}
            </div>
          </div>

          <p class="push__content">
            <router-link
              class="push__content-name"
              :to="getRouteByNotification(info)"
            >
              {{ info.author.first_name + " " + info.author.last_name }}

              {{ getNotificationsTextType(info.notification_type) }}
            </router-link>

            <span class="push__content-preview"> «{{ info.content }}»</span>
          </p>
          <span class="push__time">{{ info.sent_time | moment("from") }}</span>
        </div>
      </div>
      <router-link
        class="push__btn"
        :to="{ name: 'Push' }"
        v-if="getNotificationsLength > 0"
        >Показать ({{ getNotificationsLength }})</router-link
      >
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import { getRouteByNotification } from "@/utils/notifications.utils.js";
export default {
  name: "Push",
  props: {
    isOpen: Boolean,
  },
  computed: {
    ...mapGetters("profile/notifications", [
      "getNotifications",
      "getNotificationsLength",
      "getNotificationsTextType",
    ]),
  },
  watch: {
    isOpen(val) {
      if (val) {
        this.$refs.list.scrollTop = 0;
        this.fetchNotifications();
      } else {
        this.fetchNotificationsLength();
      }
    },
  },
  methods: {
    ...mapActions("profile/notifications", [
      "fetchNotifications",
      "fetchNotificationsLength",
    ]),
    getRouteByNotification,
    closePush() {
      if (!this.isOpen) return;
      this.$emit("close-push");
    },
  },
  mounted() {
    if (this.getNotificationsLength === 0) {
      this.fetchNotificationsLength();
    }
    if (
      window.innerHeight -
        this.$refs.wrap.getBoundingClientRect().top -
        this.$refs.wrap.offsetHeight <
      0
    ) {
      this.$refs.wrap.style.maxHeight = `${
        window.innerHeight - this.$refs.wrap.getBoundingClientRect().top
      }px`;
    }
    window.onscroll = () => {
      this.closePush();
    };
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl';

.push {
  .push__overlay {
    display: none;
  }

  &.open {
    .push__overlay {
      display: block;
    }
  }
}

.push__overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;
  cursor: default;
}

.push__wrap {
  position: fixed;
  background: #FFf;
  box-shadow: 0px 2px 60px rgba(0, 0, 0, 0.1);
  right: 50px;
  top: header-height;
  width: 100%;
  max-width: 710px;
  max-height: 675px;
  z-index: 100;
  opacity: 0;
  visibility: hidden;
  transform: translateY(-20px);
  transition: all 0.2s;
  overflow-y: auto;

  &.open {
    transform: translateY(0);
    opacity: 1;
    visibility: visible;
  }

  &:before, &:after {
    content: '';
    display: block;
    width: 19px;
    height: 38px;
    position: absolute;
    top: -16px;
  }

  &:before {
    background-image: linear-gradient(115deg, transparent 50%, #fff 50%);
    right: 223px;
  }

  &:after {
    background-image: linear-gradient(245deg, transparent 50%, #fff 50%);
    right: 205px;
  }
}

.push__list {
  overflow-y: auto;
  max-height: 450px;
}

.push__item {
  display: flex;
  align-items: center;
  padding: 35px 0;
  margin: 0 40px;
  max-height: 70px;

  &+& {
    border-top: 1px solid #E7E7E7;
  }
}

.push__btn {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 15px;
  letter-spacing: 0.01em;
  color: eucalypt;
  border-top: 1px solid #E7E7E7;
  height: 55px;
}

.main-layout__user-pic {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
  flex: none;
  background-color: #8bc49e;


  div {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  img {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}
</style>
