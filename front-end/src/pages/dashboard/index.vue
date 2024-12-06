<script setup lang="ts">
import { toast } from "vue3-toastify";
import "vue3-toastify/dist/index.css";
import { VDataTableServer } from "vuetify/labs/VDataTable";
// 👉 Store
const keyword = ref("");
const typeOut = ref<any[]>([]);
const dateFrom = ref("");
const dateTo = ref("");

// Data table options
const itemsPerPage = ref(10);
const page = ref(1);
const sortBy = ref();
const orderBy = ref();

const isUserInfoEditDialogVisible = ref(false);

const notify = () => {
  toast("Lock user Roy Keane successful!", {
    type: "success",
    theme: "colored",
    autoClose: 1000,
  }); // ToastOptions
};
// Data value
const users = ref([
  {
    id: "1",
    email: "mcarrick@dc.com",
    fullName: "Michael Carrick",
    plan: "Basic",
    status: 1,
    phoneNumber: "123-456-7890",
  },
  {
    id: "2",
    email: "rkeane@dc.com",
    fullName: "Roy Keane",
    plan: "Standard",
    status: 1,
    phoneNumber: "234-567-8901",
  },
  {
    id: "3",
    email: "rvnistelrooy@dc.com",
    fullName: "Ruud van Nistelrooy",
    status: 2,
    phoneNumber: "345-678-9012",
  },
  {
    id: "4",
    email: "dbeckham@dc.com",
    fullName: "David Beckham",
    plan: "Basic",
    status: 3,
    phoneNumber: "456-789-0123",
  },
  {
    id: "5",
    email: "gnev@dc.com",
    fullName: "Gary Neville",
    status: 2,
    phoneNumber: "567-890-1234",
  },
  {
    id: "6",
    email: "pscholes@dc.com",
    fullName: "Paul Scholes",
    plan: "Mobile",
    status: 1,
    phoneNumber: "678-901-2345",
  },
  {
    id: "7",
    email: "rferdinand@dc.com",
    fullName: "Rio Ferdinand",
    plan: "Premium",
    status: 3,
    phoneNumber: "789-012-3456",
  },
  {
    id: "8",
    email: "rvanpersie@dc.com",
    fullName: "Robin van Persie",
    status: 2,
    phoneNumber: "890-123-4567",
  },
  {
    id: "9",
    email: "wrooney@dc.com",
    fullName: "Wayne Rooney",
    status: 2,
    phoneNumber: "901-234-5678",
  },
  {
    id: "10",
    email: "jsmith@dc.com",
    fullName: "John Smith",
    plan: "Premium",
    status: 3,
    phoneNumber: "012-345-6789",
  },
]);
const totalUsers = ref(1995);
const openAlert = ref(false);
const titleAlert = ref("");

const updateStatus = (item: any, type: string) => {
  switch (type) {
    case "lock":
      titleAlert.value = "Lock user " + item.fullName;
      break;
    case "unlock":
      titleAlert.value = "Unlock user " + item.fullName;
      break;
    case "delete":
      titleAlert.value = "Delete user " + item.fullName;
      break;
  }
  openAlert.value = true;
};

// Update data table options
const updateOptions = (options: any) => {
  page.value = options.page;
  sortBy.value = options.sortBy[0]?.key;
  orderBy.value = options.sortBy[0]?.order;
  search();
};

// Headers
const headers: any = [
  { title: "No", key: "stt", sortable: false, align: "center" },
  { title: "Action", key: "actions", sortable: false, align: "center" },
  {
    title: "Email",
    key: "email",
    sortable: false,
    align: "center",
  },
  { title: "Full name", key: "fullName", sortable: false, align: "center" },
  {
    title: "Phone number",
    key: "phoneNumber",
    sortable: false,
    align: "center",
  },
  { title: "Subscription Plan", key: "plan", sortable: false, align: "center" },
  { title: "Status", key: "status", sortable: false, align: "center" },
];

const resolveStatusVariant = (status: number) => {
  if (status === 1) return { color: "success", text: "Active" };
  else if (status === 2) return { color: "warning", text: "Pending" };
  else if (status === 3) return { color: "secondary", text: "Inactive" };
};

// 👉 Fetching users
const search = async () => {
  try {
    const data = await useApi<any>(
      createUrl("/get-all", {
        query: {
          keyword: keyword,
          typeOut: typeOut ?? "",
          page: page.value - 1,
          size: itemsPerPage,
          dateFrom: dateFrom,
          dateTo: dateTo,
        },
      })
    ).then((res) => {
      return res.data.value;
    });
    if (data.code == 200) {
      users.value = data.content.items;
      totalUsers.value = data.content.total;
    } else {
      users.value = [];
      totalUsers.value = 0;
    }
  } catch (err) {
    console.error(err);
  }
};
const handleSelectChange = (selectedValue: any[]) => {
  typeOut.value = selectedValue; // Replace the entire array to trigger reactivity
};
</script>

<template>
  <section>
    <VCard class="mb-6">
      <VCardText>
        <VRow>
          <!-- 👉 Select Role -->
          <VCol>
            <AppTextField
              v-model="keyword"
              label="Email"
              placeholder="Enter email"
            />
          </VCol>
          <VCol>
            <AppTextField
              v-model="keyword"
              label="Phone number"
              placeholder="Enter phone number"
            />
          </VCol>
          <!-- <VCol>
            <AppSelect
              :items="typeDDL"
              item-title="content"
              item-value="value"
              @update:modelValue="handleSelectChange"
              label="Type action"
              placeholder="Select type action"
              :clearable="true"
            />
          </VCol>
          <VCol>
            <AppDateTimePicker
              v-model="dateFrom"
              label="From date"
              placeholder="Select date"
              :clearable="true"
            />
          </VCol>
          <VCol>
            <AppDateTimePicker
              v-model="dateTo"
              label="To date"
              placeholder="Select date"
              :clearable="true"
            />
          </VCol> -->
        </VRow>
        <VRow>
          <VCol class="text-center">
            <VBtn
              @click="
                isUserInfoEditDialogVisible = !isUserInfoEditDialogVisible
              "
              color="primary"
              class="mx-3"
            >
              Search
            </VBtn>
            <VBtn @click="notify" color="error" class="mx-3"> Delete </VBtn>
          </VCol>
        </VRow>
      </VCardText>
    </VCard>
    <VCard>
      <!-- <VCardText class="py-4 gap-4">
        <div class="text-center">
          <h2 class="text-h2 pricing-title mb-2">History</h2>
        </div>
      </VCardText> -->

      <VDivider />

      <!-- SECTION datatable -->
      <VDataTableServer
        :items-per-page="itemsPerPage"
        :page="page"
        :items="users"
        :items-length="totalUsers"
        :headers="headers"
        class="text-no-wrap"
        @update:options="updateOptions"
        show-select
      >
        <!-- User -->
        <template #item.stt="{ index }">
          <strong>{{ index + 1 + 10 * (page - 1) }}</strong>
        </template>
        <template #item.status="{ item }">
          <VChip
            :label="false"
            :color="resolveStatusVariant(item.status).color"
          >
            {{ resolveStatusVariant(item.status).text }}
          </VChip>
        </template>

        <!-- Actions -->
        <template #item.actions="{ item }">
          <IconBtn>
            <VIcon icon="tabler-eye" color="success" />
          </IconBtn>
          <IconBtn>
            <VIcon icon="tabler-edit" color="primary" />
          </IconBtn>
          <IconBtn>
            <VIcon
              :icon="item.status == 1 ? 'tabler-lock' : 'tabler-lock-open'"
              color="warning"
              @click="
                item.status == 1
                  ? updateStatus(item, 'lock')
                  : updateStatus(item, 'unlock')
              "
            />
          </IconBtn>

          <IconBtn @click="updateStatus(item, 'delete')">
            <VIcon icon="tabler-trash" color="error" />
          </IconBtn>
        </template>

        <!-- pagination -->
        <template #bottom>
          <VDivider />
          <div class="d-flex align-center gap-3 pa-5 pt-3">
            <div class="me-16 d-flex gap-3 align-center justify-start">
              <span>View</span>
              <AppSelect
                v-model="itemsPerPage"
                :items="[
                  { value: 10, title: '10' },
                  { value: 25, title: '25' },
                  { value: 50, title: '50' },
                  { value: 100, title: '100' },
                ]"
                style="inline-size: 6.25rem"
                item-title="title"
                item-value="value"
              />
              <span>records/page</span>
            </div>
            <span>Total {{ totalUsers }} records</span>
            <VPagination
              v-model="page"
              :length="Math.ceil(totalUsers / itemsPerPage)"
              :total-visible="6"
              variant="outlined"
              rounded="circle"
              :show-first-last-page="true"
            >
            </VPagination>
          </div>
        </template>
      </VDataTableServer>
      <!-- SECTION -->
    </VCard>
  </section>
  <VDialog v-model="openAlert" max-width="400px">
    <VCard>
      <VCardTitle class="text-center mt-3 text-h3">{{ titleAlert }}</VCardTitle>
      <VCardText>
        <div class="d-flex justify-center gap-4">
          <VBtn color="error" variant="outlined"> Cancel </VBtn>
          <VBtn color="success" variant="elevated"> OK </VBtn>
        </div>
      </VCardText>
    </VCard>
  </VDialog>
  <UserInfoEditDialog v-model:isDialogVisible="isUserInfoEditDialogVisible" />
</template>
