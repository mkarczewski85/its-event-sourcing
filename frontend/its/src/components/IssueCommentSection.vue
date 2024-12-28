<template>
  <v-container>
    <v-btn color="primary" prepend-icon="mdi-comment" :disabled="isIssueClosed" @click="openDialog">Komentuj</v-btn>

    <v-dialog v-model="dialog" max-width="500">
      <v-card>
        <v-card-title>
          <span class="text-h6">Dodaj komentarz</span>
        </v-card-title>
        <v-card-text>
          <v-form>
            <v-textarea
                v-model="newComment"
                counter="1048"
                label="Twój komentarz"
                outlined
                dense
                required
            ></v-textarea>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn variant="text" prepend-icon="mdi-cancel" @click="closeDialog">Anuluj</v-btn>
          <v-btn color="primary" prepend-icon="mdi-send" @click="submitComment">Wyślij</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-divider class="my-4"></v-divider>

    <div class="comments-container">
      <v-list>
        <v-list-subheader>Komentarze: </v-list-subheader>
        <v-list-item
            v-for="comment in comments"
            class="mb-4">
          <v-card variant="tonal">
            <v-card-subtitle>
              {{ comment.authoredBy }}
              <span class="grey--text text--darken-1 ml-2">
                ({{ formatDate(comment.publishedAt) }})
              </span>
            </v-card-subtitle>
            <v-card-text>
              {{ comment.content }}
            </v-card-text>
          </v-card>
        </v-list-item>
      </v-list>
    </div>

    <v-pagination
        v-model="currentPage"
        :length="totalPages"
        :total-visible="5"
        class="mt-4"
        @input="fetchComments"
    ></v-pagination>
  </v-container>
</template>

<script>
import Formatter from "@/components/utils/formatter.js";
export default {
  components: {
    Formatter
  },
  props: {
    issueId: {
      type: String,
      required: true,
    },
    isIssueClosed: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      comments: [],
      dialog: false,
      newComment: "",
      totalPages: 0,
      currentPage: 1,
    };
  },
  methods: {
    async fetchComments() {
      const offset = (this.currentPage - 1) * 5;
      try {
        const response = await this.$axios.get(
            `/api/secured/issues/${this.issueId}/comments`,
            {
              params: {
                limit: 5,
                offset,
              },
            }
        );
        const { data, totalPages } = response.data;
        this.comments = data;
        this.totalPages = totalPages;
      } catch (error) {
        console.error("Error fetching comments:", error);
      }
    },
    async submitComment() {
      if (!this.newComment.trim()) {
        return;
      }
      try {
        await this.$axios.post(`/api/secured/issues/${this.issueId}/comments`, {
          uuid: this.issueId,
          content: this.newComment.trim(),
        });
        this.newComment = "";
        this.dialog = false;
        await this.fetchComments();
      } catch (error) {
        console.error("Error submitting comment:", error);
      }
    },
    refreshComments() {
      this.fetchComments();
    },
    openDialog() {
      this.dialog = true;
    },
    closeDialog() {
      this.dialog = false;
    },
  },
  watch: {
    issueId: {
      immediate: true,
      handler() {
        this.currentPage = 1;
        this.fetchComments();
      },
    },
  },
};
</script>

<style scoped>
.comments-container {
  max-height: 400px; /* Stała wysokość listy */
  overflow-y: auto; /* Przewijanie w pionie */
  padding-right: 8px; /* Odstęp dla paska przewijania */
  margin-bottom: 16px; /* Margines poniżej */
}
</style>
