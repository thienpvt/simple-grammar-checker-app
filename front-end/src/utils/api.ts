import { ofetch } from "ofetch";

export const $api = ofetch.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/api",
  async onRequest({ options }) {
    const accessToken = useCookie("accessToken").value;
    if (accessToken) {
      options.headers = {
        ...options.headers,
        Authorization: `Bearer ${accessToken}`,
      };
    }
  },
});
export const $OpenApi = ofetch.create({
  baseURL: import.meta.env.VITE_OPEN_API_URL,
  async onRequest({ options }) {
    const tokenOpenApi = import.meta.env.VITE_OPEN_API_KEY;
    options.headers = {
      ...options.headers,
      accept: "application/json",
      Authorization: `Bearer ${tokenOpenApi}`,
    };
  },
});
