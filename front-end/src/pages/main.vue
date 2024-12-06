<script setup lang="ts">
import { VForm } from "vuetify/components/VForm";

const inputTextEl = ref<HTMLInputElement>();
const inputText = ref<string>("");

// output for grammar check
const explanation = ref<string | undefined>("");
const errorKeywords = ref<string | undefined>("");
const revisedParagraph = ref<string | undefined>("");

// output for plagiarism check
const plagiarismPercentage = ref<string | undefined>("");
const plagiarismExplanation = ref<string | undefined>("");
const plagiarismSource = ref<string | undefined>("");

// output for text completion
const completedText = ref<string | undefined>("");

// output for paraphrasing
const paraphrasedText = ref<string | undefined>("");

const refVForm = ref<VForm>();

const handleClick = async (type: string) => {
  refVForm.value?.validate().then(({ valid: isValid }) => {
    if (isValid) {
      let prompt = "";
      let endPromt = "";
      switch (type) {
        case "grammar":
          prompt =
            "Help me check grammar of this paragraph and highlight error keywords: \n\n";
          endPromt =
            "\n\nRemember to return only the format like that:Revised paragraph: [paragraph return].\n\nError keywords: [keyword1, keyword2, ...].\n\nExplanation of corrections: [* explanation1, * explanation2, ...]";
          break;

        case "plagiarism":
          prompt =
            "Check and give me percentage plagiarism of this paragraph: \n\n";
          endPromt =
            "\n\nRemember to return correctly link to source and only the format like that:Percentage of plagiarism: [percentage].\n\nExplaination: [explanation].\n\n Original source: [correctly link to source ].";
          break;

        case "text-completion":
          prompt = "Complete this paragraph: \n\n ";
          endPromt =
            "\n\nRemenber to return only the completion version with the format like: Completion Version: [the completed paragraph]";
          break;

        case "paraphrasing":
          prompt = "Paraphrase this paragraph:\n\n ";
          endPromt =
            "\n\nRemember to return only the paraphrased version with the format like: Paraphrased Version: [the paraphrased version]";
          break;
      }
      $OpenApi("", {
        method: "POST",
        body: {
          model: "mistralai/Mixtral-8x7B-Instruct-v0.1",
          frequency_penalty: 0,
          presence_penalty: 0,
          temperature: 0.7,
          top_p: 0.7,
          top_k: 50,
          repetition_penalty: 1,
          messages: [
            {
              role: "user",
              content: prompt + inputText.value + endPromt,
            },
          ],
        },
      }).then((res) => {
        const result = res.choices[0].message.content;

        console.log(result);
        if (type == "grammar") {
          revisedParagraph.value = result.split("\n\n")[0]?.trim();
          errorKeywords.value = result.split("\n\n")[1];
          explanation.value = result.split("\n\n").slice(2)?.join("\n\n");

          plagiarismExplanation.value = undefined;
          plagiarismPercentage.value = undefined;
          plagiarismSource.value = undefined;
          completedText.value = undefined;
          paraphrasedText.value = undefined;
        } else if (type == "plagiarism") {
          plagiarismPercentage.value = result.split("\n\n")[0]?.trim();

          plagiarismExplanation.value = result.split("\n\n")[1];

          plagiarismSource.value = result.split("\n\n").slice(2)?.join("\n\n");

          revisedParagraph.value = undefined;
          errorKeywords.value = undefined;
          explanation.value = undefined;
          completedText.value = undefined;
          paraphrasedText.value = undefined;
        } else if (type == "text-completion") {
          completedText.value = result;

          revisedParagraph.value = undefined;
          errorKeywords.value = undefined;
          explanation.value = undefined;
          plagiarismExplanation.value = undefined;
          plagiarismPercentage.value = undefined;
          plagiarismSource.value = undefined;
          paraphrasedText.value = undefined;
        } else if (type == "paraphrasing") {
          paraphrasedText.value = result;

          revisedParagraph.value = undefined;
          errorKeywords.value = undefined;
          explanation.value = undefined;
          plagiarismExplanation.value = undefined;
          plagiarismPercentage.value = undefined;
          plagiarismSource.value = undefined;
          completedText.value = undefined;
        }
        $api("save", {
          method: "POST",
          body: {
            type: type,
            result: result,
            inputText:inputText.value
          },
          onResponseError({ response }) {
            console.log(response._data.errors);
          },
        });
      });
    }
  });
};
</script>

<template>
  <div>
    <VCard class="mb-6">
      <!-- 👉 Title and subtitle -->
      <div class="text-center">
        <h2 class="text-h2 pricing-title mb-2">Writing assistant</h2>
      </div>
      <VForm ref="refVForm" @submit.prevent="() => {}">
        <VCol cols="12">
          <VTextarea
            ref="inputTextEl"
            v-model="inputText"
            rows="10"
            :rules="[requiredValidator]"
            class="form-control"
          />
        </VCol>
        <VCol cols="12" class="text-center">
          <VBtn
            class="mx-1 my-1"
            width="150px"
            color="primary"
            type="submit"
            @click="handleClick('grammar')"
          >
            Grammar check
          </VBtn>
          <VBtn
            class="mx-1 my-1"
            width="150px"
            color="primary"
            type="submit"
            @click="handleClick('plagiarism')"
          >
            Plagiarism check
          </VBtn>
          <VBtn
            class="mx-1 my-1"
            width="150px"
            color="primary"
            type="submit"
            @click="handleClick('text-completion')"
          >
            Text completion
          </VBtn>
          <VBtn
            class="mx-1 my-1"
            width="150px"
            color="primary"
            type="submit"
            @click="handleClick('paraphrasing')"
          >
            Paraphrasing
          </VBtn>
        </VCol>
      </VForm>
    </VCard>

    <VCard>
      <VCardText>
        <h3 class="text-h3">Result</h3>
        <!-- Grammar -->
        <h4 v-if="revisedParagraph" class="text-h4">
          {{ revisedParagraph.split(":")[0] }}:
        </h4>
        <p v-if="revisedParagraph">
          {{ revisedParagraph.split(":")[1] }}
        </p>
        <h4 v-if="errorKeywords" class="text-h4">
          {{ errorKeywords.split(":")[0] }}:
        </h4>
        <p v-if="errorKeywords" class="text-danger">
          {{ errorKeywords.split(":")[1] }}
        </p>
        <h4 v-if="explanation" class="text-h4">
          {{ explanation.split(":")[0] }}:
        </h4>
        <p v-if="explanation" class="text-danger">
          {{ explanation.split(":").slice(1).join(":") }}
          {{ explanation .split(":") .slice(1) .join(":") .split("*")
          .forEach((item) => { return `<span>${item}</span>`; }) }}
        </p>

        <!-- Plagiarism -->
        <h4 v-if="plagiarismPercentage" class="text-h4">
          {{ plagiarismPercentage.split(":")[0] }}:
        </h4>
        <p v-if="plagiarismPercentage">
          {{ plagiarismPercentage.split(":")[1] }}
        </p>
        <h4 v-if="plagiarismExplanation" class="text-h4">
          {{ plagiarismExplanation.split(":")[0] }}:
        </h4>
        <p v-if="plagiarismExplanation" class="text-danger">
          {{ plagiarismExplanation.split(":").slice(1).join(": ") }}
        </p>
        <h4 v-if="plagiarismSource" class="text-h4">
          {{ plagiarismSource.split(":")[0] }}:
        </h4>
        <p v-if="plagiarismSource" class="text-danger">
          {{ plagiarismSource.split(":").slice(1).join(": ") }}
        </p>

        <!-- Completion -->
        <h4 v-if="completedText" class="text-h4">
          {{ completedText.split(":")[0] }}:
        </h4>
        <p v-if="completedText">
          {{ completedText.split(":").slice(1).join(": ") }}
        </p>
        <!-- Paraphrasing -->
        <h4 v-if="paraphrasedText" class="text-h4">
          {{ paraphrasedText.split(":")[0] }}:
        </h4>
        <p v-if="paraphrasedText">
          {{ paraphrasedText.split(":").slice(1).join(": ") }}
        </p>
      </VCardText>
    </VCard>
  </div>
</template>

<style lang="scss" scoped></style>
