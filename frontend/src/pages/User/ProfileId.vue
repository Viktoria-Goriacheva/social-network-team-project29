<template>
  <div class="profile inner-page" v-if="getUsersInfo">
    <div class="inner-page__main">
      <div class="profile__info">
        <profile-info
          :info="getUsersInfo"
          :blocked="getUsersInfo.is_blocked"
          :friend="getUsersInfo.is_friend"
          :online="getUsersInfo.is_online"
        />
      </div>
      <div class="profile__news">
        <div class="profile__tabs">
          <span class="profile__tab active">
            Публикации {{ getUsersInfo.first_name }} ({{ getWall.length }})
          </span>
        </div>
        <div v-if="getWall && getWall.length > 0" class="profile__news-list">
          <news-block
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
import NewsBlock from "@/components/News/Block";
import Pagination from "@/components/Pagination.vue";
import { mapActions, mapGetters } from "vuex";

export default {
  name: "ProfileId",
  components: { ProfileInfo, NewsBlock, Pagination },
  data: () => ({
    loading: false,
    page: 1,
    perPage: 3,
    countNews: 0,
    activeWallNews: [],
  }),
  computed: {
    ...mapGetters("users/info", ["getUsersInfo", "getWall"]),
  },
  methods: {
    ...mapActions("users/info", ["userInfoId"]),
  },
  created() {
    this.userInfoId(this.$route.params.id);
  },
  watch: {
    page(value) {
      const cloneActiveWall = [...this.getWall];
      this.activeWallNews = cloneActiveWall.splice(
        (value - 1) * this.perPage,
        this.perPage
      );
    },
    getWall(newValue) {
      const cloneActiveWall = [...newValue];
      this.countNews = cloneActiveWall ? cloneActiveWall.length : 0;
      this.page = 1;
      this.activeWallNews = cloneActiveWall.splice(0, this.perPage);
    },
  },
};
</script>
