<template>
  <div class="news-block" :class="{ deffered, 'news-block--admin': admin }">
    <add-form
      v-if="isEditNews"
      :info="info"
      edit="edit"
      :deffered="deffered"
      @submit-complete="toggleEditNews"
      @close-form="toggleEditNews"
    />
    <template v-else>
      <template v-if="!admin">
        <div class="edit">
          <div class="edit__icon" v-if="deleted" @click="deleteNews">
            <simple-svg :filepath="'/static/img/delete-news.svg'" />
          </div>
          <div class="edit__icon" v-if="edit" @click="toggleEditNews">
            <simple-svg :filepath="'/static/img/edit.svg'" />
          </div>
        </div>
      </template>
      <template v-else>
        <div class="edit" v-tooltip.bottom="'Разблокировать'" v-if="blocked">
          <simple-svg :filepath="'/static/img/unblocked.svg'" />
        </div>
        <div class="edit" v-tooltip.bottom="'Заблокировать'" v-else>
          <simple-svg :filepath="'/static/img/blocked.svg'" />
        </div>
      </template>
      <div class="news-block__deffered" v-if="deffered">
        <span class="news-block__deffered-text">
          Дата и время публикации:
          {{ info.time | moment("DD.MM.YYYY, HH:mm") }} (будет опубликован
          {{ info.time | moment("from") }})
        </span>
      </div>
      <div class="news-block__author" v-if="!deffered">
        <router-link
          class="news-block__author-pic"
          :to="routerLink(info.author.id)"
        >
          <div class="main-layout__user-pic">
            <img
              v-if="info.author.photo"
              :src="info.author.photo"
              :alt="info.author.first_name[0] + ' ' + info.author.last_name[0]"
            />
            <div v-else>
              {{ info.author.first_name[0] + " " + info.author.last_name[0] }}
            </div>
          </div>
        </router-link>
        <div class="news-block__author-info">
          <router-link
            class="news-block__author-name"
            :to="routerLink(info.author.id)"
          >
            {{ info.author.first_name + " " + info.author.last_name }}
          </router-link>
          <span class="news-block__author-time">
            Опубликован {{ info.time | moment("from") }}
          </span>
        </div>
      </div>
      <div class="news-block__content">
        <div class="news-block__content-main">
          <h3 class="news-block__content-title">{{ info.title }}</h3>
          <div v-if="info.photo_url">
            <img class="post-image" :src="info.photo_url" :alt="'photo'" />
          </div>
          <p
            class="news-block__content-text"
            ref="text"
            :class="{ lotText: isLotText, open: openText }"
            v-html="info.post_text"
          ></p>
          <a
            class="news-block__content-more"
            href="#"
            v-if="isLotText"
            @click.prevent="toggleText"
          >
            <template v-if="openText">Скрыть</template>
            <template v-else>Читать весь пост</template>
          </a>
        </div>
        <ul
          class="news-block__content-tags"
          v-if="info.tags && info.tags.length > 0"
        >
          <li
            class="news-block__content-tag"
            v-for="(tag, index) in info.tags"
            :key="index"
          >
            <router-link
              :to="{ name: 'Search', query: { tab: 'news', tags: tag.tag } }"
            >
              {{ "#" + tag.tag }}
            </router-link>
          </li>
        </ul>
      </div>
      <div class="news-block__actions" v-if="!deffered && !admin">
        <div class="news-block__actions-block">
          <like-comment
            :quantity="info.likes"
            width="16px"
            height="16px"
            font-size="15px"
            @liked="likeAction"
            :active="info.my_like"
            :id="info.id"
          ></like-comment>
        </div>
        <div class="news-block__actions-block">
          <like-comment
            :quantity="commentsLength"
            width="16px"
            height="16px"
            font-size="15px"
            comment="comment"
          />
        </div>
      </div>
      <div class="news-block__comments" v-if="!deffered">
        <comments
          :admin="admin"
          :info="info.comments"
          :id="info.id"
          :edit="edit"
          :deleted="deleted"
        />
      </div>
    </template>
  </div>
</template>

<script>
import AddForm from "@/components/News/AddForm";
import { mapActions, mapGetters } from "vuex";
import Comments from "@/components/Comments/Index.vue";
import LikeComment from "@/components/LikeComment";

export default {
  name: "NewsBlock",
  components: { Comments, LikeComment, AddForm },
  props: {
    info: {
      type: Object,
      default: () => ({
        title: "Дизайн привычных вещей",
        post_text:
          "А вот и «Книга недели от Skillbox и МИФ». Сегодня делимся с вами книгой «Дизайн привычных вещей» автора Дональда Нормана. В ней Дональд рассказывает об основополагающих принципах, которым нужно следовать, чтобы избежать проблем и превратить привычные вещи в приятные товары, доставляющие нам удовольствие А вот и «Книга недели от Skillbox и МИФ». Сегодня делимся с вами книгой «Дизайн привычных вещей» автора Дональда Нормана. В ней Дональд рассказывает об основополагающих принципах, которым нужно следовать, чтобы избежать проблем и превратить привычные вещи в приятные товары, доставляющие нам удовольствие",
        time: 1559751301818,
        likes: 44,
        id: 1,
        tags: ["tag1"],
        comments: [],
        author: "",
      }),
    },
    edit: Boolean,
    deffered: Boolean,
    admin: Boolean,
    blocked: Boolean,
    deleted: Boolean,
  },
  data: () => ({
    isLotText: false,
    openText: false,
    isEditNews: false,
  }),
  computed: {
    ...mapGetters("profile/info", ["getInfo"]),
    commentsLength() {
      let result = 0;
      this.info.comments.map((el) => {
        !el.is_deleted && result++;
        el.sub_comments &&
          el.sub_comments.map((subEl) => {
            !subEl.is_deleted && result++;
          });
      });
      return result;
    },
  },
  methods: {
    ...mapActions("global/likes", ["putLike", "deleteLike"]),
    ...mapActions("profile/feeds", ["deleteFeeds"]),
    toggleText() {
      this.openText = !this.openText;
    },
    likeAction(active) {
      active
        ? this.deleteLike({ item_id: this.info.id, type: "POST" })
        : this.putLike({ item_id: this.info.id, type: "POST" });
    },
    toggleEditNews() {
      this.isEditNews = !this.isEditNews;
    },
    routerLink(infoAuthorId) {
      if (this.getInfo.id === infoAuthorId) return { name: "Profile" };
      else return { name: "ProfileId", params: { id: infoAuthorId } };
    },
    deleteNews() {
      this.deleteFeeds({
        id: this.getInfo.id,
        post_id: this.info.id,
        route: this.$route.name,
      });
    },
  },
  mounted() {
    this.$refs.text.offsetHeight > 100
      ? (this.isLotText = true)
      : (this.isLotText = false);
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl';

.news-block {
  background: #FFFFFF;
  box-shadow: standart-boxshadow;
  padding: 30px 40px 10px;
  position: relative;
  border-radius: 3%;
  &.deffered {
    padding-bottom: 20px;

    .edit {
      top: 30px;
    }
  }

  &--admin {
    .news-block__comments {
      margin-top: 20px;
      margin-bottom: 20px;
    }
  }

  & + & {
    margin-top: 30px;
  }

  @media (max-width: breakpoint-xxl) {
    padding: 20px 30px 10px;
  }
}

.news-block__deffered {
  position: relative;
  height: 45px;
  margin-bottom: 20px;
  display: flex;

  &:after {
    content: '';
    display: block;
    position: absolute;
    left: -40px;
    right: -40px;
    bottom: 0;
    width: calc(100% + 80px);
    height: 2px;
    background-color: #e7e7e7;
  }

  @media (max-width: breakpoint-xxl) {
    &:after {
      left: -30px;
      width: calc(100% + 60px);
    }
  }
}

.news-block__deffered-text {
  color: #5F5E7A;
  font-size: 16px;
}

.news-block__author {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.news-block__author-pic {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.news-block__author-name {
  font-weight: 600;
  font-size: 15px;
  color: #000;
  display: block;
  margin-bottom: 5px;
}

.news-block__author-time {
  font-size: 13px;
  color: santas-gray;
}

.news-block__content {
  display: flex;
}

.news-block__content-main {
  padding-bottom: 20px;
  border-bottom: 1px solid #E7E7E7;
  width: 100%;
}

.news-block__content-title {
  font-family: font-exo;
  font-weight: bold;
  font-size: 24px;
  color: #000;
  margin-bottom: 10px;
}

.news-block__content-text {
  font-size: 15px;
  line-height: 25px;
  color: storm-gray;
  overflow: hidden;
  text-align: justify;
  padding-right: 1em;
  position: relative;

  &:before {
    content: '\02026';
    position: absolute;
    right: 0;
    bottom: 0;
  }

  &:after {
    content: '';
    position: absolute;
    right: 0;
    width: 1em;
    height: 1em;
    margin-top: 0.2em;
    background: white;
  }

  &.lotText {
    max-height: 100px;
  }

  &.open {
    max-height: 100%;
    padding-right: 0;
  }
}

.news-block__content-more {
  display: inline-block;
  margin-top: 10px;
  font-size: 13px;
  color: eucalypt;
}

.news-block__content-tags {
  background-color: #F5F7FB;
  padding: 20px;
  max-width: 230px;
  flex: none;
  align-self: flex-start;
  margin-left: 40px;

  @media (max-width: breakpoint-xxl) {
    margin-left: 20px;
  }
}

.news-block__content-tag {
  color: eucalypt;
  font-size: 13px;
  line-height: 22px;
  display: inline-block;
  margin: 0 7px;
}

.news-block__actions {
  display: flex;
  align-items: center;
  margin: 25px 0;
}

.news-block__actions-block {
  & + & {
    margin-left: 30px;
  }
}
</style>
