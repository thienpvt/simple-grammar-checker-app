<script setup lang="ts">
interface Props {
  userData?: any;
  isDialogVisible: boolean;
}

interface Emit {
  (e: "submit", value: any): void;
  (e: "update:isDialogVisible", val: boolean): void;
}

const props = withDefaults(defineProps<Props>(), {
  userData: () => ({
    id: 0,
    fullName: "Michael Carrick",
    company: "",
    role: "",
    username: "",
    country: "",
    contact: "123-456-7890",
    email: "mcarrick@dc.com",
    currentPlan: "",
    status: 1,
    avatar: "",
    taskDone: null,
    projectDone: null,
    taxId: "",
    language: "",
    subscriptionPlan: 1,
  }),
});

const emit = defineEmits<Emit>();

const userData = ref<any>(structuredClone(toRaw(props.userData)));
const isUseAsBillingAddress = ref(false);

watch(props, () => {
  userData.value = structuredClone(toRaw(props.userData));
});

const onFormSubmit = () => {
  emit("update:isDialogVisible", false);
  emit("submit", userData.value);
};

const onFormReset = () => {
  userData.value = structuredClone(toRaw(props.userData));

  emit("update:isDialogVisible", false);
};

const dialogModelValueUpdate = (val: boolean) => {
  emit("update:isDialogVisible", val);
};
</script>

<template>
  <VDialog
    :width="$vuetify.display.smAndDown ? 'auto' : 677"
    :model-value="props.isDialogVisible"
    @update:model-value="dialogModelValueUpdate"
  >
    <!-- Dialog close btn -->
    <DialogCloseBtn @click="dialogModelValueUpdate(false)" />

    <VCard class="pa-sm-8 pa-5">
      <VCardItem class="text-center">
        <VCardTitle class="text-h3 mb-3"> Edit User Information </VCardTitle>
        <p class="mb-0">Updating user details will receive a privacy audit.</p>
      </VCardItem>

      <VCardText>
        <!-- 👉 Form -->
        <VForm class="mt-6" @submit.prevent="onFormSubmit">
          <VRow>
            <!-- 👉 First Name -->
            <VCol cols="12" md="6">
              <AppTextField
                v-model="userData.fullName"
                label="Full Name"
                placeholder="John Doe"
              />
            </VCol>

            <!-- 👉 Billing Email -->
            <VCol cols="12" md="6">
              <AppTextField
                label="Email"
                :placeholder="userData.email"
                disabled
              />
            </VCol>

            <!-- 👉 Status -->
            <VCol cols="12" md="6">
              <AppSelect
                label="Subscription Plan"
                v-model="userData.subscriptionPlan"
                :items="[
                  { value: 1, title: 'Basic' },
                  { value: 2, title: 'Standard' },
                  { value: 5, title: 'Premium' },
                ]"
                item-title="title"
                item-value="value"
              />
            </VCol>
            <!-- 👉 Status -->
            <VCol cols="12" md="6">
              <AppSelect
                v-model="userData.status"
                label="Status"
                :items="[
                  { value: 1, title: 'Active' },
                  { value: 2, title: 'Inactive' },
                  { value: 3, title: 'Pending' },
                ]"
                item-title="title"
                item-value="value"
              />
            </VCol>

            <!-- 👉 Contact -->
            <VCol cols="12" md="6">
              <AppTextField
                v-model="userData.contact"
                label="Phone number"
                placeholder="+1 9876543210"
              />
            </VCol>

            <!-- 👉 Country -->
            <VCol cols="12" md="6">
              <AppTextField
                v-model="userData.country"
                label="Country"
                placeholder="United States"
                disabled
              />
            </VCol>

            <!-- 👉 Switch -->

            <!-- 👉 Submit and Cancel -->
            <VCol cols="12" class="d-flex flex-wrap justify-center gap-4">
              <VBtn type="submit"> Submit </VBtn>

              <VBtn color="secondary" variant="tonal" @click="onFormReset">
                Cancel
              </VBtn>
            </VCol>
          </VRow>
        </VForm>
      </VCardText>
    </VCard>
  </VDialog>
</template>
