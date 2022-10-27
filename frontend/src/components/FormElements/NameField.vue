<template lang="pug">
  .form__group(:class="{fill: name.length > 0}")
    input.form__input_stylus(:id="id" v-model="name" name="name" :class="{invalid: (v.$dirty && !v.required) || (v.$dirty && !v.minLength)}" @change="v.$touch()")
    label.form__label_stylus(:for="id") {{label}}
    span.form__error(v-if="v.$dirty && !v.required") Обязательное поле
    span.form__error(v-else-if="v.$dirty && !v.minLength") Минимальное количество символов {{v.minLength}}
</template>

<script>
export default {
  name: "NameField",
  props: {
    value: {
      type: String,
      default: "",
    },
    v: {
      type: Object,
      required: true,
    },
    label: {
      type: String,
      default: "Имя",
    },
    id: {
      type: String,
      required: true,
    },
  },
  computed: {
    name: {
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
