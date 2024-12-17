<template>
  <v-dialog v-model="localOpen" :max-width="maxWidth">
    <v-card>
      <v-card-title class="text-h5">
        {{ this.title }}
        <v-spacer></v-spacer>
      </v-card-title>
      <v-card-text>
        <v-select
            :label="label"
            class="ma-2 pa-2"
            :items="options"
            variant="outlined"
            v-model="localSelectedOption"
            required></v-select>
        <slot />
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" :disabled="confirmBtnDisabled" prepend-icon="mdi-content-save" @click="handleUpdate">Zapisz</v-btn>
        <v-btn color="secondary" prepend-icon="mdi-cancel" @click="close">Anuluj</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import Dictionary from "@/components/utils/dictionary.js";
import StatusRules from "@/components/utils/status_rule.js";

export default {
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
    updatedElement: {
      type: String,
      required: true
    },
    maxWidth: {
      type: String,
      default: "600px",
    },
    currentState: {
      type: Object,
      required: true
    }
  },
  emits: ["update:isOpen", "confirm"],
  data() {
    return {
      confirmBtnDisabled: true,
      localSelectedOption: this.currentOption
    };
  },
  computed: {
    localOpen: {
      get() {
        return this.isOpen;
      },
      set(value) {
        this.$emit("update:isOpen", value);
      },
    },
    label: {
      get() {
        if (this.updatedElement === 'severity') {
          return "Priorytet zgłoszenia";
        } else if (this.updatedElement === 'type') {
          return "Rodzaj zgłoszenia";
        } else {
          return "Status zgłoszenia";
        }
      }
    },
    title: {
      get() {
        if (this.updatedElement === 'severity') {
          return "Edycja priorytetu zgłoszenia";
        } else if (this.updatedElement === 'type') {
          return "Edycja rodzaju zgłoszenia";
        } else {
          return "Edycja statusu zgłoszenia";
        }
      }
    },
    options: {
      get() {
        if (this.updatedElement === 'severity') {
          return  Dictionary.severityOptions;
        } else if (this.updatedElement === 'type') {
          return Dictionary.typeOptions;
        } else {
          return StatusRules.resolveAllowedNextStatuses(this.currentState.status)
        }
      }
    },
    currentOption: {
      get() {
        if (this.updatedElement === 'status') return '';
        return this.currentState[this.updatedElement];
      }
    }
  },
  methods: {
    handleValueSelect() {
      this.confirmBtnDisabled = this.localSelectedOption === this.currentOption ||
               this.localSelectedOption === null;
    },
    handleUpdate() {
      if (this.updatedElement === 'status') {
        this.handleStatusUpdate();
      } else {
        this.handleIssuePatch();
      }
    },
    async handleStatusUpdate() {
      try {
        const uuid = this.currentState.uuid;
        const response = await this.$axios.put(`/api/secured/issues/${uuid}/status`, {
          uuid: uuid,
          status: this.localSelectedOption
        });
        console.log('Issue status updated:', response.data);
        this.confirm();
      } catch (error) {
        console.error('Failed to add issue:', error);
      }
    },
    async handleIssuePatch() {
      try {
        const uuid = this.currentState.uuid;
        const response = await this.$axios.patch(`/api/secured/issues/${uuid}`, {
          uuid: uuid,
          severity: this.updatedElement === 'severity' ? this.localSelectedOption : null,
          type: this.updatedElement === 'type' ? this.localSelectedOption : null
        });
        console.log('Issue status patched:', response.data);
        this.confirm();
      } catch (error) {
        console.error('Failed to add issue:', error);
      }
    },
    close() {
      this.localOpen = false;
      this.localSelectedOption = null;
    },
    confirm() {
      this.$emit("confirm");
      this.close();
    },
  },
  watch: {
    localSelectedOption: 'handleValueSelect'
  }
};
</script>
