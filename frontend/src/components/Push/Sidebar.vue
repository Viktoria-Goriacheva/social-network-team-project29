<template lang="pug">
  .aside-filter-notes
    h2.aside-filter-notes__title Оповещения
    ul.aside-filter-notes__list
      li.aside-filter-notes__item(v-for="item in list" :key="item" :class="{active: item === value}" @click="changePushSidebar(item)") {{item}}
</template>

<script>
export default {
  name: "PushSidebar",
  props: {
    value: String,
  },
  data: () => ({
    list: ["Все", "Комментарии", "Друзья"],
  }),
  methods: {
    changePushSidebar(item) {
      this.$emit("change-push-sidebar", item);
    },
  },
};
</script>
