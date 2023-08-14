// Created by LiuYang On 2023/5/28 15:34
import * as components from "@element-plus/icons-vue";
export default {
    install: (app) => {
        for (const key in components) {
            const componentConfig =
                components[key];

            app.component(componentConfig.name,
                componentConfig);
        }
    },
};