<template lang="pug">
  .form__group(:class="{fill: password.length > 0}")
    label.form__label_stylus(:for="id") Повторите пароль
    input.form__input_stylus(name="password" type="password" :id="id"
      v-model.trim="password"
      @change="v.$touch()"
      :class="{invalid: (v.$dirty && !v.required) || (v.$dirty && !v.minLength) || (v.$dirty && !v.sameAsPassword)}"
    )
    span.form__error(v-if="v.$dirty && !v.sameAsPassword") Пароли должны совпадать
    span.form__error(v-if="v.$dirty && !v.minLength") Пароль должен быть не менее {{v.$params.minLength.min}} символов. Сейчас он {{password.length}}
</template>

<script>
export default {
  name: "PasswordRepeatField",
  props: {
    value: {
      type: String,
      default: "",
    },
    v: {
      type: Object,
      required: true,
    },
    id: {
      type: String,
      required: true,
    },
  },
  computed: {
    password: {
      get() {
        return this.value;
      },
      set(value) {
        this.$emit("input", value);
      },
    },
  },
};
</script>
