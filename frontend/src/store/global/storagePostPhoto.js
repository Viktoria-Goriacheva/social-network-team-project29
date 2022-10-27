import axios from "axios";

export default {
  namespaced: true,
  state: {
    storagePostPhoto: null,
  },
  getters: {
    getStoragePostPhoto: (s) => s.storagePostPhoto,
  },
  mutations: {
    setStoragePostPhoto: (s, value) => {
      s.storagePostPhoto = value;
    },
  },
  actions: {
    async apiStoragePostPhoto({ commit }, file) {
      let formData = new FormData();
      formData.append("file", file);
      await axios({
        url: "storagePostPhoto?type=IMAGE",
        method: "POST",
        data: formData,
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
        .then((response) => {
          commit("setStoragePostPhoto", response.data.data);
        })
        .catch(() => {});
    },
  },
};
