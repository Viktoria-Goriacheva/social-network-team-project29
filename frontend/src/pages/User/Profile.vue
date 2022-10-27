<template>
  <div class="profile inner-page">
    <div class="inner-page__main">
      <div class="profile__info">
        <profile-info me="me" online="online" :info="getInfo"></profile-info>
      </div>
      <div class="profile__news">
        <div class="profile__tabs profile__tab" @click="changeTab('')">
          Мои публикации
        </div>
        <div class="profile__tabs">
          <span
            class="profile__tab"
            @click="changeTab('POSTED')"
            :class="{ active: activeTab === 'POSTED' }"
          >
            Опубликованные ({{ getWallPostedLength }})
          </span>
          <span
            class="profile__tab"
            @click="changeTab('QUEUED')"
            :class="{ active: activeTab === 'QUEUED' }"
            v-if="getWallQueuedLength > 0"
          >
            Отложенные ({{ getWallQueuedLength }})
          </span>
        </div>
        <div class="profile__add">
          <news-add />
        </div>
        <div class="profile__news-list">
          <news-block
            edit="edit"
            deleted="deleted"
            :deffered="activeTab === 'QUEUED'"
            v-for="news in activeWallNews"
            :key="news.id"
            :info="news"
          />
          <Pagination :count="countNews" v-model="page" :per-page="perPage" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProfileInfo from "@/components/Profile/Info";
import NewsAdd from "@/components/News/Add";
import NewsBlock from "@/components/News/Block";
import Pagination from "@/components/Pagination.vue";
import { mapGetters, mapActions } from "vuex";
export default {
  name: "Profile",
  components: { ProfileInfo, NewsAdd, NewsBlock, Pagination },
  data: () => ({
    activeTab: "POSTED",
    page: 1,
    perPage: 3,
    countNews: 0,
    activeWallNews: [],
  }),
  computed: {
    ...mapGetters("profile/info", ["getInfo"]),
    ...mapGetters("users/info", [
      "getWall",
      "getWallPostedLength",
      "getWallQueuedLength",
    ]),
    activeWall() {
      return this.getWall.filter((el) => el.type === this.activeTab);
    },
  },
  methods: {
    ...mapActions("users/info", ["apiWall"]),
    ...mapActions("profile/info", ["apiInfo"]),

    changeTab(tab) {
      this.activeTab = tab;
    },
  },
  watch: {
    page(value) {
      const cloneActiveWall = [...this.activeWall];
      this.activeWallNews = cloneActiveWall.splice(
        (value - 1) * this.perPage,
        this.perPage
      );
    },
    activeWall(newValue) {
      const cloneActiveWall = [...newValue];
      this.countNews = cloneActiveWall ? cloneActiveWall.length : 0;
      this.page = 1;
      this.activeWallNews = cloneActiveWall.splice(0, this.perPage);
    },
  },
  created() {
    if (!this.getInfo) {
      this.apiInfo();
    }
    this.apiWall({});
  },
};
</script>
