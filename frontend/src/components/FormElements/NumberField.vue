<template lang="pug">
  .form__group(:class="{fill: number.length > 0}")
    input.form__input_stylus(:id="id" v-model="number" name="number" :class="{invalid: (v.$dirty && !v.required) || (v.$dirty && !v.isCode)}" @change="v.$touch()")
    label.form__label_stylus(:for="id") Цифры
    span.form__error(v-if="v.$dirty && !v.required") Обязательное поле
    span.form__error(v-else-if="v.$dirty && !v.isCode") цифры не совпадают
</template>

<script>
export default {
  name: "NumberField",
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
    number: {
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
