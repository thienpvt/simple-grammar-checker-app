import type { RouteRecordRaw } from "vue-router/auto";

// 👉 Redirects
export const redirects: RouteRecordRaw[] = [
  // ℹ️ We are redirecting to different pages based on role.
  // NOTE: Role is just for UI purposes. ACL is based on abilities.
  {
    path: "/",
    name: "index",
    redirect: (to) => {
      // TODO: Get type from backend
      const userData = useCookie<Record<string, unknown> | null | undefined>(
        "userData"
      );
      // console.log(userData);
      if (userData.value?.role == "client") {
        // console.log("Client");
        return { name: "dashboard" };
      }
      return { name: "login" };
    },
  },
];

export const routes: RouteRecordRaw[] = [
  // Email filter
];
