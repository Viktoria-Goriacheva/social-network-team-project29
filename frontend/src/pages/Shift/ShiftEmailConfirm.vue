<template lang="pug">
  .shift-password
    form.shift-password__form(@submit.prevent="submitHandler")
      .success-shift-email
        simple-svg(:filepath="'/static/img/success-forgot.svg'")
      .form__block
        h4.form__subtitle Для подтверждения новой почты нажимайте на кнопку.
      .shift-btn__action
        button-hover(tag="button" type="submit" variant="white") Подтвердить
</template>

<script>
import axios from "axios";

export default {
  name: "ShiftEmailSuccess",

  data: () => ({}),
  computed: {},
  methods: {
    submitHandler() {
      axios({
        url: "/auth/confirm-email ",
        data: { temp: this.temp },
        method: "PUT",
      }).then((response) => {
        console.log(response);
        alert("Почта удачно подтвеждена!");
      });
    },
  },
  mounted() {
    this.temp = this.$route.query.temp || "";
  },
};
</script>

<style lang="stylus">
.shift-password {
  display: flex;
  justify-content: center;
  flex-direction: column;
  height: 70%;
}
.shift-btn__action {
  margin-top: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.success-shift-email {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  .simple-svg-wrapper {
    width: 120px;
  }
}
</style>
