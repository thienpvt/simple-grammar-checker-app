<script setup lang="ts">
import { Actions, Subjects } from "@/plugins/casl/ability";
import { ExtractSubjectType, MongoQuery, SubjectRawRule } from "@casl/ability";
import { useGenerateImageVariant } from "@core/composable/useGenerateImageVariant";
import authV2LoginIllustrationBorderedDark from "@images/pages/auth-v2-login-illustration-bordered-dark.png";
import authV2LoginIllustrationBorderedLight from "@images/pages/auth-v2-login-illustration-bordered-light.png";
import authV2LoginIllustrationDark from "@images/pages/auth-v2-login-illustration-dark.png";
import authV2LoginIllustrationLight from "@images/pages/auth-v2-login-illustration-light.png";
import authV2MaskDark from "@images/pages/misc-mask-dark.png";
import authV2MaskLight from "@images/pages/misc-mask-light.png";
import { VNodeRenderer } from "@layouts/components/VNodeRenderer";
import { themeConfig } from "@themeConfig";
import { decodeCredential, googleOneTap } from "vue3-google-login";

const router = useRouter();
const ability = useAbility();
const userAbilities: SubjectRawRule<
  Actions,
  ExtractSubjectType<Subjects>,
  MongoQuery
>[] = [
  {
    action: "manage",
    subject: "all",
  },
];

const logicLogin = (response: any) => {
  const userData: any = decodeCredential(response.credential);
  userData.role = "client";
  console.log("userData", userData);
  useCookie("accessToken").value = response.credential;
  useCookie("userData").value = JSON.stringify(userData);
  useCookie("userAbilityRules").value = JSON.stringify(userAbilities);
  ability.update(userAbilities);
  setTimeout(() => {
    router.push("/");
  }, 100);
};

googleOneTap()
  .then((response: any) => {
    // This promise is resolved when user selects an account from the the One Tap prompt
    logicLogin(response);
  })
  .catch((error) => {
    console.log("Handle the error", error);
  });

const callback = (response: any) => {
  // This callback will be triggered when user click on the One Tap prompt
  // This callback will be also triggered when user click on login button
  // and selects or login to his Google account from the popup
  logicLogin(response);
};

definePage({
  meta: {
    layout: "blank",
    unauthenticatedOnly: true,
  },
});

const authThemeImg = useGenerateImageVariant(
  authV2LoginIllustrationLight,
  authV2LoginIllustrationDark,
  authV2LoginIllustrationBorderedLight,
  authV2LoginIllustrationBorderedDark,
  true
);

const authThemeMask = useGenerateImageVariant(authV2MaskLight, authV2MaskDark);
</script>

<template>
  <VRow no-gutters class="auth-wrapper bg-surface">
    <VCol md="8" class="d-none d-md-flex">
      <div class="position-relative bg-background rounded-lg w-100 ma-8 me-0">
        <div class="d-flex align-center justify-center w-100 h-100">
          <VImg
            max-width="505"
            :src="authThemeImg"
            class="auth-illustration mt-16 mb-2"
          />
        </div>

        <VImg class="auth-footer-mask" :src="authThemeMask" />
      </div>
    </VCol>

    <VCol
      cols="12"
      md="4"
      class="auth-card-v2 d-flex align-center justify-center"
    >
      <VCard flat :max-width="500" class="mt-12 mt-sm-0 pa-4">
        <VCardText>
          <VNodeRenderer :nodes="themeConfig.app.logo" class="mb-6" />
          <h4 class="text-h4 mb-1">
            Welcome to
            <span class="text-capitalize">{{ themeConfig.app.title }}</span
            >! 👋🏻
          </h4>
          <p class="mb-0">
            Please sign-in to your account and start the adventureee
          </p>
        </VCardText>
        <VCardText>
          <VRow>
            <!-- email -->

            <!-- password -->
            <VCol cols="12" class="text-center">
              <!-- <GoogleLogin :callback="callback">
                <VBtn color="primary" variant="tonal" cols="12">
                  Login Using Google <i class="fa fa-google ml-1 text-white"></i
                ></VBtn>
              </GoogleLogin> -->
              <GoogleLogin :callback="callback" class="form-control" />
              <!-- <GoogleLogin :callback="callback" @error="handleError" /> -->
            </VCol>
          </VRow>
        </VCardText>
      </VCard>
    </VCol>
  </VRow>
</template>

<style lang="scss">
@use "@core/scss/template/pages/page-auth.scss";
</style>
