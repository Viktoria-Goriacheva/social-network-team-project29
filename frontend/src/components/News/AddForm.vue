<template lang="pug">
  .news-add-form
    .news-add__actions-buttons
      button.close_modal.bold(@click.prevent="closeAddForm") x
    form.news-add__main(action="#" @submit.prevent="" ref="form")
      .news-add__text
        textarea.news-add__text-title(type="textarea" placeholder="Дайте тему" v-model="title")
        div(v-if="src")
          img.post-image(:src="src" :alt="'photo'")
        div
          input(class="user-info-form__input_stylus-photo" type="file" ref="photo" id="photo" @change="processFile($event)" accept="image/*")
        editor-content.news-add__text-main(:editor="editor")
        editor-menu-bar.news-add__actions(:editor="editor" v-slot="{ commands, isActive, getMarkAttrs, menu }" @mouseleave.native="hideLinkMenu")
          .news-add__actions-buttons
            button.bold(:class="{ 'is-active': isActive.bold() }" @click="commands.bold") ж
            button.italic(:class="{ 'is-active': isActive.italic() }" @click="commands.italic") к
            button.underline(:class="{ 'is-active': isActive.underline() }" @click="commands.underline") ч
            .news-add__actions-link
              .news-add__actions-link-hidden(:class="{'is-active': isOpenLinkMenu}")
                form(@submit.prevent="setLinkUrl(commands.link, linkUrl)")
                  input(type="text" v-model="linkUrl" placeholder="https://" ref="linkInput" @keydown.esc="hideLinkMenu")
                  button(type="button" @click="setLinkUrl(commands.link, linkUrl)")
                    simple-svg(:filepath="'/static/img/add.svg'")
            div.news-add__block.photo(@click.prevent="loadPhoto")
                simple-svg(:filepath="'/static/img/photo.svg'")
            div.news-add__block(v-if="src")
              button.bold(@click.prevent="deletePhoto") x
      .news-add__settings
        h4.news-add__settings-title Настройка публикации
        add-tags(:tags="tags" @change-tags="onChangeTags")
        div.is_planing(v-if="isPlaning")
          h6 Запланированное время
          p {{day }} {{month.text }} {{year }}, {{time }}
        button-hover.news-add__planning(variant="white" bordered @click.native="openModal" v-if="!edit || deffered") Запланировать
        button-hover(@click.native="submitForm") Опубликовать
    modal.news-add__modal(v-model="modalShow")
      div(v-model="planingTime" @input="onChangeDatePicker" title-position="left" :min-date='new Date()' is-inline :attributes='attrs' :key="componentKey")
      .news-add__modal-selects
        select.select.news-add__modal-select.day(v-model="day" @change="changeDate")
          option(v-for="d in days" :key="d") {{d}}
        select.select.news-add__modal-select.month(v-model="month" @change="changeDate")
          option(v-for="month in months" :key="month.val" :value="month") {{month.text}}
        select.select.news-add__modal-select.year(v-model="year" @change="changeDate")
          option(v-for="i in years" :key="i") {{i}}
        select.select.news-add__modal-select.time(v-model="time" @change="changeDate")
          option(v-for="t in times" :key="t") {{t}}
      template(slot="actions")
        button-hover(@click.native="onPlaning") Планировать
        button-hover(variant="red" bordered @click.native="onCancelPlaning") Отмена
</template>

<script>
import { Editor, EditorContent, EditorMenuBar } from "tiptap";
import { Bold, Italic, Underline, Link } from "tiptap-extensions";
import { mapGetters, mapActions, mapMutations } from "vuex";
import moment from "moment";
import AddTags from "@/components/News/AddTags";
import Modal from "@/components/Modal";
export default {
  name: "NewsAddForm",
  props: {
    edit: Boolean,
    deffered: Boolean,
    info: Object,
  },
  components: { AddTags, EditorContent, EditorMenuBar, Modal },
  data: () => ({
    title: "",
    tags: [],
    editor: null,
    linkUrl: "",
    isOpenLinkMenu: false,
    modalShow: false,
    isPlaning: false,
    planingTime: new Date(),
    componentKey: 0,
    photo: "",
    src: "",
    attrs: [
      {
        key: "weekends",
        content: "weekends",
        dates: {
          start: new Date(2018, 0, 1),
          end: new Date(2022, 0, 1),
          weekdays: [1, 7],
        },
      },
    ],
    day: 1,
    month: { val: 0, text: "Января" },
    year: 2000,
    months: [
      { val: 0, text: "Января" },
      { val: 1, text: "Февраля" },
      { val: 2, text: "Марта" },
      { val: 3, text: "Апреля" },
      { val: 4, text: "Мая" },
      { val: 5, text: "Июня" },
      { val: 6, text: "Июля" },
      { val: 7, text: "Августа" },
      { val: 8, text: "Сентября" },
      { val: 9, text: "Октября" },
      { val: 10, text: "Ноября" },
      { val: 11, text: "Декабря" },
    ],
    time: "12:00",
    times: [
      "8:00",
      "9:00",
      "10:00",
      "11:00",
      "12:00",
      "13:00",
      "14:00",
      "15:00",
      "16:00",
      "17:00",
      "18:00",
      "19:00",
      "20:00",
      "21:00",
      "22:00",
      "23:00",
    ],
  }),
  computed: {
    ...mapGetters("profile/info", ["getInfo"]),
    years() {
      return Array.from({ length: 60 }, (value, index) => 1970 + index);
    },
    days() {
      return this.month.val === 2
        ? this.year & 3 || (!(this.year % 25) && this.year & 15)
          ? 28
          : 29
        : 30 + ((this.month.val + (this.month.val >> 3)) & 1);
    },
  },
  watch: {
    planingTime(val) {
      console.log(val);
      this.day = moment(val).date();
      this.month = this.months[moment(val).month()];
      this.year = moment(val).year();
      this.time = `${moment(val).hour()}:00`;
    },
  },
  methods: {
    ...mapActions("profile/feeds", ["actionsFeed"]),
    ...mapActions("global/storagePostPhoto", ["apiStoragePostPhoto"]),
    ...mapGetters("global/storagePostPhoto", ["getStoragePostPhoto"]),
    ...mapMutations("global/storagePostPhoto", ["setStoragePostPhoto"]),
    onChangeTags(tags) {
      this.tags = tags;
    },
    async submitForm() {
      if (this.title.length <= 3 || this.editor.getHTML().length <= 7) {
        this.modalShow && this.closeModal();
        this.$store.dispatch("global/alert/setAlert", {
          status: "response",
          text: "Тема должен состоять не менее трех символов, текст - не менее семи.",
        });
        return;
      }
      if (this.photo) {
        await this.apiStoragePostPhoto(this.photo).then(() => {
          if (
            this.getStoragePostPhoto() &&
            this.getStoragePostPhoto().photo_url
          ) {
            this.photo_url = this.getStoragePostPhoto().photo_url;
          }
        });
      } else if (!this.src) {
        this.photo_url = "";
      } else if (this.info && this.info.photo_url) {
        this.photo_url = this.info.photo_url;
      }
      await this.actionsFeed({
        photo_url: this.photo_url,
        route: this.$route.name,
        post_id: this.info ? this.info.id : null,
        edit: this.edit,
        id: this.getInfo.id,
        title: this.title,
        post_text: this.editor.getHTML(),
        tags: this.tags,
        publish_date:
          this.isPlaning &&
          moment({
            years: this.year,
            months: this.month.val,
            date: this.day,
            hours: this.time.substring(0, 2),
          }).valueOf() / 1000,
      }).then(() => {
        this.$emit("submit-complete");
        this.setStoragePostPhoto(null);
      });
    },
    loadPhoto() {
      this.$refs.photo.click();
    },
    deletePhoto() {
      this.photo = "";
      this.src = "";
      this.setStoragePostPhoto("");
    },
    processFile(event) {
      this.photo = event.target.files[0];
      const reader = new window.FileReader();
      reader.onload = (e) => (this.src = e.target.result);
      reader.readAsDataURL(this.photo);
    },
    openLinkMenu(attrs) {
      this.linkUrl = attrs.href;
      this.isOpenLinkMenu = true;
      this.$nextTick(() => {
        this.$refs.linkInput.focus();
      });
    },
    setLinkUrl(command, url) {
      command({ href: url });
      this.isOpenLinkMenu = false;
      this.editor.focus();
    },
    hideLinkMenu() {
      this.linkUrl = null;
      this.isOpenLinkMenu = false;
    },
    openModal() {
      this.modalShow = true;
    },
    closeModal() {
      this.modalShow = false;
    },
    onPlaning() {
      console.log(this.planingTime);
      console.log(new Date());
      if (this.planingTime < new Date()) {
        this.$store.dispatch("global/alert/setAlert", {
          status: "validation",
          text: "Запланированное время не может быть в прошлом времени!",
        });
        return;
      }
      this.isPlaning = true;
      this.modalShow = false;
    },
    onCancelPlaning() {
      this.isPlaning = false;
      this.year = 2000;
      this.month = { val: 0, text: "Января" };
      this.day = 1;
      this.time = "12:00";
      this.modalShow = false;
    },
    closeAddForm() {
      this.$emit("close-form");
    },
    setInfo(val) {
      this.day = moment(val).date();
      this.month = this.months[moment(val).month()];
      this.year = moment(val).year();
    },
    changeDate() {
      console.log("changeDate", this.year, this.month.val, this.day, this.time);
      this.componentKey += 1;
      this.planingTime = new Date(
        this.year,
        this.month.val,
        this.day,
        this.time.split(":")[0].toString()
      );
    },
    onChangeDatePicker(value) {
      this.day = moment(value).date();
      this.month = this.months[moment(value).month()];
      this.year = moment(value).year();
    },
  },
  mounted() {
    if (this.info && this.info.photo_url) {
      this.src = this.info.photo_url;
    }
    if (this.edit) {
      this.title = this.info.title;
      this.tags = this.info.tags.map((i) => i.tag);
      this.editor = new Editor({
        content: `${this.info.post_text}`,
        extensions: [new Bold(), new Italic(), new Underline(), new Link()],
      });
      if (this.deffered) {
        this.isPlaning = true;
        this.day = moment(this.info.time).date();
        this.month = this.months[moment(this.info.time).month()];
        this.year = moment(this.info.time).year();
        console.log(moment.unix(this.info.time));
        this.planingTime = moment.unix(this.info.time);

        // planingTime(val) {
        //   console.log(val);
        //   this.day = moment(val).date();
        //   this.month = this.months[moment(val).month()];
        //   this.year = moment(val).year();
        // },
      }
    } else {
      this.editor = new Editor({
        content: "<p>к черту добрые слова!</p>",
        extensions: [new Bold(), new Italic(), new Underline(), new Link()],
      });
      this.day = moment().date();
      this.month = this.months[moment().month()];
      this.year = moment().year();
    }
  },
  beforeDestroy() {
    this.editor.destroy();
  },
};
</script>
<style media="screen">
.close_modal {
  position: relative;
  top: 8px;
  left: 95%;
  width: 25px;
}
.is_planing {
  margin-bottom: 7px;
  display: flex;
  flex-direction: column;
}
.post-image {
  width: 250px;
  margin-bottom: 30px;
}
</style>
