<template>
  <div id="app">
    <component :is="layout" v-if="$route.meta.layout">
      <router-view />
    </component>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import FormLayout from "@/layouts/FormLayout";
import MainLayout from "@/layouts/MainLayout";
import EmptyLayout from "@/layouts/EmptyLayout";

export default {
  name: "App",
  components: {
    FormLayout,
    MainLayout,
    EmptyLayout,
  },
  watch: {
    "alert.show"(value) {
      if (!value) {
        return;
      }
      if (this.$store.state.global.alert.status === "success") {
        this.$vToastify.success(this.$store.state.global.alert.text);
      } else if (this.$store.state.global.alert.status === "response") {
        this.$vToastify.success(
          this.$store.state.global.alert.text,
          "Ответ запроса"
        );
      } else if (this.$store.state.global.alert.status === "action") {
        this.$vToastify.success(
          this.$store.state.global.alert.text,
          "Действие невозможно"
        );
      } else if (this.$store.state.global.alert.status === "validation") {
        this.$vToastify.success(
          this.$store.state.global.alert.text,
          "Неверные параметры"
        );
      } else {
        this.$vToastify.error(this.$store.state.global.alert.text, "Ошибка");
      }
    },
  },
  computed: {
    ...mapGetters("global/alert", ["getState"]),
    alert() {
      return this.$store.state.global.alert;
    },
    layout() {
      return this.$route.meta.layout + "-layout";
    },
  },
};
</script>
<style lang="stylus">

@import './assets/stylus/main.styl';

.v-snack__wrapper {
  &.success {
    background-color: eucalypt;
  }

  &.error {
    background-color: wild-watermelon;
  }
}
</style>
<style lang="css">
@import "../public/css/style.min.css";
::-webkit-scrollbar {
  width: 3px;
}

::-webkit-scrollbar-track {
  background-color: darkgrey;
}

::-webkit-scrollbar-thumb {
  box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.2);
}
</style>
