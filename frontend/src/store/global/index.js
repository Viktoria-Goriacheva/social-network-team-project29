import search from "./search";
import menu from "./menu";
import likes from "./likes";
import alert from "./alert";
import storage from "./storage";
import storagePostPhoto from "./storagePostPhoto";

export default {
  namespaced: true,
  modules: {
    search,
    menu,
    likes,
    alert,
    storage,
    storagePostPhoto,
  },
};
