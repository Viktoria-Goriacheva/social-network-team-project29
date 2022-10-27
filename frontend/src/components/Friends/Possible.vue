<template lang="pug">
  .friends-possible
    h4.friends-possible__title(v-if="possibleFriends.length > 0") Возможно вы их знаете
    ul.friends-possible__list
      li.friends-possible__item(v-for="user in possibleFriends" :key="user.id")
        .main-layout__user-pic
          img(v-if="user.photo" :src="user.photo" :alt="user.first_name")
          div(v-else class="avatar") {{user.first_name[0] + " " + user.last_name[0]}}
        router-link.friends-possible__name(:to="{name: 'ProfileId', params: {id: user.id}}") {{user.first_name + ' ' + user.last_name}}
        a.friends-possible__link(href="#" @click.prevent="apiAddFriends(user.id)") Добавить
    router-link.friends-possible__btn(href="#" :to="{name: 'FriendsFind'}")
      simple-svg(:filepath="'/static/img/search.svg'")
      span.friends-possible__link Искать друзей
</template>

<script>
import { mapGetters, mapActions } from "vuex";
export default {
  name: "FriendsPossible",
  computed: {
    ...mapGetters("profile/friends", ["getResultById"]),
    possibleFriends() {
      return this.getResultById("recommendations");
    },
  },
  methods: {
    ...mapActions("profile/friends", ["apiAddFriends", "apiRecommendations"]),
  },
  mounted() {
    if (this.possibleFriends.length === 0) this.apiRecommendations();
  },
};
</script>
