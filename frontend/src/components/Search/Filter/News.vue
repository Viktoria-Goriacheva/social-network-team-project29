<template>
  <div class="search-filter">
    <div class="search-filter__block">
      <label class="search__label" for="search-news-author">Автор:</label>
      <input
        class="search__input"
        type="text"
        id="search-news-author"
        v-model="author"
      />
    </div>
    <div class="search-filter__block time">
      <label class="search__label">Время публикации:</label>
      <select class="select search-filter__select" v-model="date_from">
        <option value="null">За все время</option>
        <option value="year">За последний год</option>
        <option value="month">За последний месяц</option>
        <option value="week">За последнюю неделю</option>
      </select>
    </div>
    <div class="search-filter__block tags">
      <add-tags :tags="tags" @change-tags="onChangeTags"></add-tags>
    </div>
    <div class="search-filter__block btn-news">
      <button-hover @click.native="onSearchNews">Применить</button-hover>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import moment from "moment";
import AddTags from "@/components/News/AddTags";

export default {
  name: "SearchFilterNews",
  components: { AddTags },
  data: () => ({
    tags: [],
    date_from: "year",
    date_to: 0,
    page: 1,
    itemPerPage: 5,
    author: "",
  }),
  computed: {
    ...mapGetters("global/search", ["getNewsQueryParams", "searchText"]),
  },
  methods: {
    ...mapActions("global/search", ["searchNews"]),
    onChangeTags(tags) {
      this.tags = tags;
      this.$router.replace({
        query: {
          ...this.$route.query,
          tags: tags.toString(),
        },
      });
    },
    onSearchNews() {
      const date_from =
        this.date_from === "null"
          ? 0
          : moment().subtract(1, this.date_from).valueOf();
      let searchQuery = Object.assign({}, this.getNewsQueryParams, {
        date_from: Math.floor(date_from / 1000),
        date_to: Math.floor(this.date_to / 1000),
        author: this.author,
        tags: this.tags,
        text: this.searchText,
      });
      this.searchNews(searchQuery);
    },
  },
  watch: {
    "$route.query.tags": {
      immediate: true,
      handler(value) {
        if (value) {
          this.tags = value.split(",");
        }
      },
    },
  },
  mounted() {
    this.date_to = moment().valueOf();
  },
};
</script>
