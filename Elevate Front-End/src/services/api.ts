import axios from 'axios';

const api = axios.create({
  // No baseURL — requests are relative (e.g. /api/auth/login).
  // Vite dev proxy forwards /api/* → http://localhost:8080 (same-origin, no CORS).
  headers: {
    'Content-Type': 'application/json',
  },
});

// ——— Request Interceptor: attach JWT ———
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('elevate_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ——— Response Interceptor: handle 401 ———
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('elevate_token');
      localStorage.removeItem('elevate_user');
      // Only redirect if not already on auth pages
      if (
        !window.location.pathname.includes('/login') &&
        !window.location.pathname.includes('/register')
      ) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
