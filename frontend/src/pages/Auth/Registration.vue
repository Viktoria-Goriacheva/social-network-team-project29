<template lang="pug">
  .registration
    form.registration__form(@submit.prevent="submitHandler")
      .form__block
        h4.form__subtitle Аккаунт
        email-field(id="register-email" v-model="email" :v="$v.email" :class="{checked: $v.email.required && $v.email.email}")
        password-field(id="register-password" v-model="passwd1" :v="$v.passwd1" info registration :class="{checked: $v.passwd1.required && $v.passwd2.sameAsPassword && $v.passwd1.minLength}")
        password-repeat-field(id="register-repeat-password" v-model="passwd2" :v="$v.passwd2" :class="{checked: $v.passwd1.required && $v.passwd2.sameAsPassword && $v.passwd2.minLength}")
      .form__block
        h4.form__subtitle Личные данные
        name-field(id="register-firstName" v-model="firstName" :v="$v.firstName")
        name-field(id="register-lastName" v-model="lastName" :v="$v.lastName" label="Фамилия")
      .form__block
        h4.form__subtitle Введите символы, которые вы видите на экране
        button.btn__update.bold(@click.prevent="updateCatcha") Обновить
        .img_captcha
          img(:src="imgCode" :alt="'image'")
        code-field(id="register-code" v-model="code" :v="$v.code" :class="{checked: $v.code.required && $v.code.isCode}")
        confirm-field(id="register-confirm" v-model="confirm" :v="$v.confirm")
      .registration__action
        button-hover(tag="button" type="submit" variant="white") Зарегистрироваться
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import { required, email, minLength, sameAs } from "vuelidate/lib/validators";
import PasswordField from "@/components/FormElements/PasswordField";
import PasswordRepeatField from "@/components/FormElements/PasswordRepeatField";
import EmailField from "@/components/FormElements/EmailField";
import NameField from "@/components/FormElements/NameField";
import CodeField from "@/components/FormElements/CodeField";
import ConfirmField from "@/components/FormElements/ConfirmField";

export default {
  name: "Registration",
  components: {
    PasswordField,
    EmailField,
    NameField,
    CodeField,
    ConfirmField,
    PasswordRepeatField,
  },
  data: () => ({
    email: "",
    passwd1: "",
    passwd2: "",
    firstName: "",
    lastName: "",
    imgCode: "",
    code: "",
    token: "",
    confirm: false,
    isCode: true,
  }),
  computed: {
    ...mapGetters("auth/captcha", ["getCaptcha"]),
  },
  methods: {
    ...mapActions("auth/api", ["register"]),
    ...mapActions("auth/captcha", ["fetchCaptcha"]),
    updateCatcha() {
      this.fetchCaptcha().then(() => {
        this.imgCode = this.getCaptcha.imgCode;
        this.token = this.getCaptcha.secret;
      });
    },
    submitHandler() {
      if (this.$v.$invalid) {
        this.$v.$touch();
        return;
      }
      const { email, passwd1, passwd2, firstName, lastName, code, token } =
        this;
      this.register({
        email,
        passwd1,
        passwd2,
        firstName,
        lastName,
        code,
        token,
      })
        .then(() => {
          this.$router.push({ name: "RegisterSuccess" });
        })
        .catch((error) => {
          const errorMessage = error.response.data.error_description[0] || "";
          if (errorMessage === "Неверный код авторизации") {
            this.isCode = false;
          }
        });
    },
  },
  beforeMount() {
    this.updateCatcha();
  },
  mounted() {
    // this.code = this.getCode;
  },
  watch: {
    code() {
      if (this.isCode === false) {
        this.isCode = true;
      }
    },
  },
  validations: {
    confirm: { sameAs: sameAs(() => true) },
    email: { required, email },
    passwd1: { required, minLength: minLength(8) },
    passwd2: {
      required,
      minLength: minLength(8),
      sameAsPassword: sameAs("passwd1"),
    },
    firstName: { required, minLength: minLength(3) },
    lastName: { required, minLength: minLength(3) },
    code: {
      required,
      isCode() {
        return this.isCode;
      },
    },
  },
};
</script>

<style lang="stylus">
@import '../../assets/stylus/base/vars.styl';

.registration {
  min-height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.registration__action {
  margin-top: 40px;

  @media (max-width: breakpoint-xxl) {
    margin-top: 20px;
  }
}

.img_captcha {
  margin-bottom: 15px;
}

.btn__update {
  margin-bottom: 15px;
  width: 90px;
  height: 30px;
  padding: 1px;
  background-color: white;
  color: #21a45d
}
</style>
