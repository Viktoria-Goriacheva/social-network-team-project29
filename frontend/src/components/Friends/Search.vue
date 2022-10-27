<template lang="pug">
  form.friends-possible(@submit.prevent="onSearchUsers" action="#")
    h4.friends-possible__title Параметры поиска
    .friends-search
      .friends-search__row
        .friends-search__block
          label.search__label(for="friends-search-name") Имя:
          input.search__input(type="text" id="friends-search-name" v-model="first_name")
        .friends-search__block
          label.search__label(for="friends-search-lastname") Фамилия:
          input.search__input(type="text" id="friends-search-lastname" v-model="last_name")
      .friends-search__block
        label.search__label Возраст:
        .search__row
          select.select.friends-search__select(v-model.number="age_from")
            option(value="null" disabled) От
            option(v-for="age in ageFromArray" :value="age" :key="age") От {{ age }}

          span.search__age-defis —
          select.select.friends-search__select(v-model.number="age_to")
            option(value="null" disabled) До
            option(v-for="age in ageToArray" :value="age" :key="age") От {{ age }}
      .friends-search__block
        label.search__label Регион:
        .search__row
          select.select.friends-search__select(v-model="country")
            option(value="null" :disabled="disabled") Страна
            option(v-for="c in countries" :key="c.id" :value="c") {{ c.title }}
          select.select.friends-search__select(v-model="city")
            option(value="null" disabled="disabled") Город
            option(v-for="c in cities" :key="c.id" :value="c") {{ c.title }}
    button.friends-possible__btn(type="submit")
      simple-svg(:filepath="'/static/img/search.svg'")
      span.friends-possible__link Искать друзей
</template>

<script>
import axios from "axios";
import { mapActions } from "vuex";
export default {
  name: "FriendsSearch",
  data: () => ({
    first_name: null,
    last_name: null,
    age_from: null,
    age_to: null,
    country: null,
    city: null,
    disabled: false,
    countries: [],
    cities: [],
    page: 1,
    itemSize: 5,
    ageFromArray: [...Array(100).keys()].slice(5),
    ageToArray: [...Array(121).keys()].slice(5),
  }),
  methods: {
    ...mapActions("global/search", ["searchUsers", "clearSearch"]),
    onSearchUsers() {
      let { first_name, last_name, age_from, age_to } = this;
      if (age_from !== null && age_to !== null && age_from >= age_to) {
        this.$store.dispatch("global/alert/setAlert", {
          status: "error",
          text: "Неправильный выбор возрастного интервала!",
        });
        return;
      }
      let countryId = this.country ? this.country.id : null;
      let cityId = this.city ? this.city.id : null;
      this.searchUsers({
        first_name,
        last_name,
        age_from,
        age_to,
        country: countryId,
        city: cityId,
      });
    },

    loadCountries() {
      axios
        .get("/geo/countries")
        .then((response) => {
          this.countries = response.data.data;
        })
        .catch(() => {});
    },
    loadCities(countryId) {
      if (!countryId) return;
      axios.get(`/geo/cities/${countryId}`).then((response) => {
        this.cities = response.data.data;
      });
    },
  },
  beforeDestroy() {
    this.clearSearch();
  },
  mounted() {
    this.loadCountries();
  },
  watch: {
    country: {
      immediate: true,
      handler(value) {
        if (value && value.id) this.loadCities(value.id);
        else this.cities = [];
        this.city = null;
      },
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl';

.friends-search {
  margin-top: 25px;
  padding-top: 20px;
  margin-bottom: 30px;
  border-top: 1px solid #E6E6E6;
}

.friends-search__row {
  @media (max-width: breakpoint-xl) {
    display: flex;

    .friends-search__block {
      flex: auto;
    }

    .friends-search__block + .friends-search__block {
      margin-top: 0;
      margin-left: 12px;
    }
  }
}

.friends-search__row + .friends-search__block {
  margin-top: 15px;
}

.friends-search__block {
  &+& {
    margin-top: 15px;
  }
}

.friends-search__select {
  display: block;
  width: 100%;

  &+& {
    margin-left: 12px;
  }
}
</style>
