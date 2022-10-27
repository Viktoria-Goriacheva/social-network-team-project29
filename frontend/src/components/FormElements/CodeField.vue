<template lang="pug">
  .form__group(:class="{fill: code.length > 0}")
    input.form__input_stylus(:id="id" v-model="code" name="code" :class="{invalid: (v.$dirty && !v.required) || (v.$dirty && !v.isCode)}" @change="v.$touch()")
    label.form__label_stylus(:for="id") Код
    span.form__error(v-if="v.$dirty && !v.required") Обязательное поле
    span.form__error(v-else-if="v.$dirty && !v.isCode") код не совпадает
</template>

<script>
export default {
  name: "CodeField",
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
    code: {
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
