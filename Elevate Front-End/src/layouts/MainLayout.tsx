import { Outlet } from 'react-router-dom';
import Navbar from '../components/ui/Navbar';

export default function MainLayout() {
  return (
    <div className="min-h-screen flex flex-col bg-surface-950">
      <Navbar />
      <main className="flex-1">
        <Outlet />
      </main>
      <footer className="border-t border-surface-800/60 py-6 mt-auto">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 flex flex-col sm:flex-row items-center justify-between gap-3">
          <p className="text-xs text-surface-500">
            © {new Date().getFullYear()} Elevate. All rights reserved.
          </p>
          <div className="flex items-center gap-4">
            <a href="#" className="text-xs text-surface-500 hover:text-surface-300 transition-colors">Privacy</a>
            <a href="#" className="text-xs text-surface-500 hover:text-surface-300 transition-colors">Terms</a>
            <a href="#" className="text-xs text-surface-500 hover:text-surface-300 transition-colors">Help</a>
          </div>
        </div>
      </footer>
    </div>
  );
}
