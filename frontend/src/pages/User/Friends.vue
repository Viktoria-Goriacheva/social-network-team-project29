<template>
  <div class="friends inner-page">
    <div class="inner-page__main">
      <div class="friends__header">
        <h2 class="friends__title">Мои друзья</h2>
        <div class="friends__search">
          <div class="friends__search-icon">
            <simple-svg :filepath="'/static/img/search.svg'" />
          </div>
          <input
            class="friends__search-input"
            placeholder="Начните вводить имя друга..."
            v-model="first_name"
          />
        </div>
      </div>
      <div class="friends__list">
        <div v-if="friendsRequestFrom.length > 0" class="friends_group">
          <h3 class="friends_group_title">Запросы в друзья</h3>
          <friends-block
            friend="friend"
            v-for="friend in friendsRequestFrom"
            :key="friend.id"
            :info="friend"
            accept-button
          />
        </div>

        <div v-if="friendsRequestTo.length > 0" class="friends_group">
          <h3 class="friends_group_title">Исходящие запросы</h3>
          <friends-block
            friend="friend"
            v-for="friend in friendsRequestTo"
            :key="friend.id"
            :info="friend"
          />
        </div>

        <div v-if="friendsFriend.length > 0" class="friends_group">
          <h3 class="friends_group_title">Друзья</h3>
          <friends-block
            friend="friend"
            v-for="friend in friendsFriend"
            :key="friend.id"
            :info="friend"
          />
        </div>

        <div v-if="friendsSubscribed.length > 0" class="friends_group">
          <h3 class="friends_group_title">Подписчики</h3>
          <friends-block
            friend="friend"
            v-for="friend in friendsSubscribed"
            :key="friend.id"
            :info="friend"
          />
        </div>

        <div v-if="friendsBlocked.length > 0" class="friends_group">
          <h3 class="friends_group_title">Заблокированные пользователи</h3>
          <friends-block
            friend="friend"
            v-for="friend in friendsBlocked"
            :key="friend.id"
            :info="friend"
            :blocked="true"
          />
        </div>

        <div v-if="friendsRejecting.length > 0" class="friends_group">
          <h3 class="friends_group_title">Отклоненные запросы</h3>
          <friends-block
            friend="friend"
            v-for="friend in friendsRejecting"
            :key="friend.id"
            :info="friend"
          />
        </div>

        <div v-if="friendsWatching.length > 0" class="friends_group">
          <h3 class="friends_group_title">Подписан(а)</h3>
          <friends-block
            friend="friend"
            v-for="friend in friendsWatching"
            :key="friend.id"
            :info="friend"
          />
        </div>
      </div>
      <div class="inner-page__aside">
        <friends-possible />
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import FriendsPossible from "@/components/Friends/Possible";
import FriendsBlock from "@/components/Friends/Block";

export default {
  name: "Friends",
  components: { FriendsPossible, FriendsBlock },
  data: () => ({
    first_name: "",
    local_friends: [],
  }),
  computed: {
    ...mapGetters("profile/friends", ["getResultById"]),
    friends() {
      return this.getResultById("friends");
    },
    friendsRequestFrom() {
      return this.local_friends.filter((f) => f.statusCode === "REQUEST_FROM");
    },
    friendsRequestTo() {
      return this.local_friends.filter((f) => f.statusCode === "REQUEST_TO");
    },
    friendsFriend() {
      return this.local_friends.filter((f) => f.statusCode === "FRIEND");
    },
    friendsBlocked() {
      return this.local_friends.filter((f) => f.statusCode === "BLOCKED");
    },
    friendsRejecting() {
      return this.local_friends.filter((f) => f.statusCode === "REJECTING");
    },
    friendsSubscribed() {
      return this.local_friends.filter((f) => f.statusCode === "SUBSCRIBED");
    },
    friendsWatching() {
      return this.local_friends.filter((f) => f.statusCode === "WATCHING");
    },
  },
  watch: {
    friends() {
      this.local_friends = this.friends;
    },
    first_name(value) {
      if (this.first_name === "") this.local_friends = this.friends;
      else
        this.local_friends = this.local_friends.filter(
          (f) => f.first_name.includes(value) || f.last_name.includes(value)
        );
    },
  },

  methods: {
    ...mapActions("profile/friends", ["apiFriends"]),
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.apiFriends();
    });
  },
};
</script>

<style scoped>
.friends_group_title {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
  margin-top: 20px;
  color: #666;
}
.friends_group {
  max-height: 600px;
  overflow-y: scroll;
  background-color: #eaeaea47;
  margin-top: 40px;
  border: 0.5px solid #eaeaea;
}
</style>
