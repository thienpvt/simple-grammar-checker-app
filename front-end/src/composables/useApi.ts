import { router } from "@/plugins/1.router";
import { createFetch } from "@vueuse/core";
import { destr } from "destr";

export const useApi = createFetch({
  baseUrl: import.meta.env.VITE_API_BASE_URL || "/api",
  fetchOptions: {
    headers: {
      Accept: "application/json, text/plain, */*",
    },
  },
  options: {
    refetch: true,
    async beforeFetch({ options }) {
      const accessToken = useCookie("accessToken").value;

      if (accessToken) {
        options.headers = {
          ...options.headers,
          Authorization: `Bearer ${accessToken}`,
        };
      }

      return { options };
    },
    afterFetch(ctx) {
      const { data, response } = ctx;

      // Parse data if it's JSON
      let parsedData = null;
      try {
        parsedData = destr(data);
      } catch (error) {
        console.error(error);
      }

      return { data: parsedData, response };
    },
    onFetchError(ctx) {
      const { data, response, error } = ctx;
      if (response?.status === 401) {
        useCookie("accessToken").value = undefined;
        setTimeout(() => {
          router.push("/login");
        }, 200);
      }
      return { data: response, error };
    },
  },
});