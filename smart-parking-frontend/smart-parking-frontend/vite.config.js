import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Dev server runs on port 5173 by default.
// The backend (Spring Boot) runs on port 8080 — see src/api/client.js for the base URL.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173
  }
})
