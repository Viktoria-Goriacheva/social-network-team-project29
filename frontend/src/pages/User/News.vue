<template>
  <div class="news inner-page">
    <div class="inner-page__main">
      <div class="news__add">
        <news-add user="user"></news-add>
      </div>
      <div class="news__list" v-if="getInfo">
        <news-block
          v-for="feed in getFeeds"
          :key="feed.id"
          :info="feed"
          :edit="getInfo.id === feed.author.id"
          :deleted="getInfo.id === feed.author.id"
        />
      </div>
      <Pagination :count="total" v-model="page" :per-page="size" />
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapMutations, mapState } from "vuex";
import NewsBlock from "@/components/News/Block";
import NewsAdd from "@/components/News/Add";
import Pagination from "@/components/Pagination.vue";

export default {
  name: "News",
  components: { NewsBlock, NewsAdd, Pagination },
  data() {
    return {
      page: 1,
      size: 5,
      total: 20,
    };
  },
  computed: {
    ...mapGetters("profile/feeds", ["getFeeds", "getFeedsPagination"]),
    ...mapState("profile/feeds", ["feedsPagination"]),
    ...mapGetters("profile/info", ["getInfo"]),
  },
  methods: {
    ...mapActions("profile/feeds", ["apiFeeds"]),
    ...mapMutations("profile/feeds", ["setFeeds"]),
  },
  watch: {
    page() {
      this.apiFeeds({
        size: this.size,
        page: this.page - 1,
      });
    },
    getFeedsPagination(pag) {
      // this.page = pag.page;
      // this.size = pag.size;
      this.total = pag.total;
    },
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.apiFeeds({
        size: vm.getFeedsPagination.size,
        page: vm.getFeedsPagination.page - 1,
      });
    });
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl';

.news__add {
  margin-bottom: 30px;
}
.news__list {
  margin-bottom: 30px;
}
</style>
