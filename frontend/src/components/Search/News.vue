<template>
  <div class="search-news">
    <search-block title="Новости" id="news">
      <news-block v-for="n in news" :key="n.id" :info="n" />
    </search-block>
    <Pagination :count="total" v-model="page" :per-page="size" />
  </div>
</template>

<script>
import SearchBlock from "@/components/Search/Block";
import NewsBlock from "@/components/News/Block";
import { mapGetters, mapActions } from "vuex";
import Pagination from "@/components/Pagination.vue";

export default {
  name: "SearchNews",
  components: { SearchBlock, NewsBlock, Pagination },
  data() {
    return {
      page: 1,
      size: 5,
      total: 20,
    };
  },
  computed: {
    ...mapGetters("global/search", [
      "getResultById",
      "getNewsQueryParams",
      "getNewsPagination",
    ]),
    news() {
      return this.getResultById("news");
    },
  },
  methods: {
    ...mapActions("global/search", ["searchNews"]),
  },
  watch: {
    page() {
      let searchQuery = Object.assign({}, this.getNewsQueryParams, {
        page: this.page - 1,
        size: this.size,
      });
      this.searchNews(searchQuery);
    },
    getNewsPagination() {
      // this.page = this.getNewsPagination.page;
      // this.perPage = this.getNewsPagination.perPage;
      this.total = this.getNewsPagination.total;
    },
  },
  mounted() {
    const tags = this.$route.query.tags;
    let searchQuery = Object.assign({}, this.getNewsQueryParams, {
      page: this.page - 1,
      size: this.size,
    });
    if (tags) searchQuery.tags = tags;
    if (this.news.length === 0) {
      this.searchNews(searchQuery);
    }
  },
};
</script>
