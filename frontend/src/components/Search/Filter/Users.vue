<template>
  <div class="search-filter">
    <div class="search-filter__block name">
      <label class="search__label" for="search-people-name">Имя:</label>
      <input
        class="search__input"
        type="text"
        id="search-people-name"
        v-model="first_name"
      />
    </div>
    <div class="search-filter__block lastname">
      <label class="search__label" for="search-people-lastname">Фамилия:</label>
      <input
        class="search__input"
        type="text"
        id="search-people-lastname"
        v-model="last_name"
      />
    </div>
    <div class="search-filter__block age">
      <label class="search__label">Возраст:</label>
      <div class="search__row">
        <select class="select search-filter__select" v-model.number="age_from">
          <option :value="null" :disabled="disabled">От</option>
          <option v-for="age in ageFromArray" :value="age" :key="age">
            От {{ age }}
          </option>
        </select>
        <span class="search__age-defis">—</span>
        <select class="select search-filter__select" v-model.number="age_to">
          <option :value="null" :disabled="disabled">До</option>
          <option v-for="age in ageToArray" :value="age" :key="age">
            До {{ age }}
          </option>
        </select>
      </div>
    </div>
    <div class="search-filter__block region">
      <label class="search__label">Регион:</label>
      <div class="search__row">
        <select class="select search-filter__select" v-model="country">
          <option :value="null" :disabled="disabled">Страна</option>
          <option v-for="c in countries" :key="c.id" :value="c">
            {{ c.title }}
          </option>
        </select>
        <select class="select search-filter__select" v-model="city">
          <option :value="null" :disabled="disabled">Город</option>
          <option v-for="c in cities" :key="c.id" :value="c">
            {{ c.title }}
          </option>
        </select>
      </div>
    </div>
    <div class="search-filter__block btn-news" @click.prevent="onSearchUsers">
      <button-hover>Применить</button-hover>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "SearchFilterUsers",
  data: () => ({
    first_name: null,
    last_name: null,
    age_from: null,
    age_to: null,
    country: null,
    city: null,
    countries: [],
    disabled: false,
    cities: [],
    ageFromArray: [...Array(100).keys()].slice(5),
    ageToArray: [...Array(121).keys()].slice(5),
  }),
  computed: {
    ...mapGetters("global/search", ["getUsersQueryParams", "searchText"]),
  },
  methods: {
    ...mapActions("global/search", ["searchUsers"]),
    onSearchUsers() {
      let { first_name, last_name, age_from, age_to } = this;
      if (age_from !== null && age_to !== null && age_from >= age_to) {
        this.$store.dispatch("global/alert/setAlert", {
          status: "action",
          text: "Неправильный выбор возрастного интервала!",
        });
        return;
      }
      let countryId = this.country ? this.country.id : null;
      let cityId = this.city ? this.city.id : null;
      let searchQuery = Object.assign({}, this.getUsersQueryParams, {
        first_name,
        last_name,
        age_from,
        age_to,
        country: countryId,
        city: cityId,
        author: this.searchText,
      });
      this.searchUsers(searchQuery);
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
