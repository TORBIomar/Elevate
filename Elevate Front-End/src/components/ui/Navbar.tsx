import { useState, useRef, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import {
  Briefcase,
  LogOut,
  User,
  Menu,
  X,
  Plus,
  LayoutDashboard,
  Search,
  ChevronDown,
  CalendarIcon,
  FileText,
  Bookmark,
} from 'lucide-react';
import NotificationBell from './NotificationBell';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);
  const profileRef = useRef<HTMLDivElement>(null);

  // Close profile dropdown on outside click
  useEffect(() => {
    function handleClick(e: MouseEvent) {
      if (profileRef.current && !profileRef.current.contains(e.target as Node)) {
        setProfileOpen(false);
      }
    }
    document.addEventListener('mousedown', handleClick);
    return () => document.removeEventListener('mousedown', handleClick);
  }, []);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isActive = (path: string) => location.pathname.startsWith(path);

  const navLinks = [
    ...(user?.role !== 'RECRUITER' 
      ? [{ to: '/jobs', label: 'Job Board', icon: <Search className="w-4 h-4" /> }] 
      : []),
    ...(user?.role === 'RECRUITER'
      ? [
          { to: '/recruiter/dashboard', label: 'Dashboard', icon: <LayoutDashboard className="w-4 h-4" /> },
          { to: '/recruiter/interviews', label: 'Interviews', icon: <CalendarIcon className="w-4 h-4" /> },
        ]
      : user?.role === 'CANDIDATE' 
      ? [
          { to: '/my-applications', label: 'My Applications', icon: <FileText className="w-4 h-4" /> },
          { to: '/my-interviews', label: 'My Interviews', icon: <CalendarIcon className="w-4 h-4" /> },
          { to: '/my-saved-jobs', label: 'Saved Jobs', icon: <Bookmark className="w-4 h-4" /> },
      ] : []),
  ];

  const initials = user ? `${user.firstName[0]}${user.lastName[0]}`.toUpperCase() : '?';

  return (
    <header className="sticky top-0 z-40 w-full glass-strong">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="flex h-16 items-center justify-between">
          {/* Logo */}
          <Link to="/jobs" className="flex items-center gap-2.5 shrink-0 group">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-primary-500 to-accent-500 flex items-center justify-center shadow-lg shadow-primary-500/20 group-hover:shadow-primary-500/40 transition-shadow">
              <Briefcase className="w-5 h-5 text-white" />
            </div>
            <span className="text-xl font-bold tracking-tight">
              <span className="gradient-text">Elevate</span>
            </span>
          </Link>

          {/* Desktop Nav */}
          <nav className="hidden md:flex items-center gap-1">
            {navLinks.map((link) => (
              <Link
                key={link.to}
                to={link.to}
                className={`
                  flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all
                  ${
                    isActive(link.to)
                      ? 'bg-primary-500/10 text-primary-400'
                      : 'text-surface-400 hover:text-surface-200 hover:bg-surface-800/60'
                  }
                `}
              >
                {link.icon}
                {link.label}
              </Link>
            ))}
          </nav>

          {/* Right side */}
          <div className="flex items-center gap-3">
            {/* Notifications */}
            {user && <NotificationBell />}

            {/* Role badge */}
            <span className="hidden sm:inline-flex text-xs font-semibold px-2.5 py-1 rounded-full bg-primary-500/10 text-primary-400 border border-primary-500/20">
              {user?.role}
            </span>

            {/* Profile dropdown */}
            <div className="relative" ref={profileRef}>
              <button
                id="profile-menu-button"
                onClick={() => setProfileOpen(!profileOpen)}
                className="flex items-center gap-2 p-1.5 rounded-xl hover:bg-surface-800/60 transition-all"
              >
                <div className="w-8 h-8 rounded-full bg-gradient-to-br from-primary-500 to-accent-500 flex items-center justify-center text-white text-xs font-bold">
                  {initials}
                </div>
                <ChevronDown className={`w-4 h-4 text-surface-400 hidden sm:block transition-transform ${profileOpen ? 'rotate-180' : ''}`} />
              </button>

              {profileOpen && (
                <div className="absolute right-0 mt-2 w-56 rounded-xl glass-strong shadow-2xl shadow-black/40 py-2 animate-slide-down">
                  <div className="px-4 py-3 border-b border-surface-700/40">
                    <p className="text-sm font-semibold text-surface-200">{user?.firstName} {user?.lastName}</p>
                    <p className="text-xs text-surface-500 truncate">{user?.email}</p>
                  </div>
                  <Link
                    to="/profile"
                    onClick={() => setProfileOpen(false)}
                    className="flex items-center gap-2.5 px-4 py-2.5 text-sm text-surface-300 hover:text-surface-100 hover:bg-surface-800/60 transition-all"
                  >
                    <User className="w-4 h-4" />
                    Profile
                  </Link>
                  <button
                    onClick={handleLogout}
                    className="w-full flex items-center gap-2.5 px-4 py-2.5 text-sm text-red-400 hover:text-red-300 hover:bg-red-500/5 transition-all"
                  >
                    <LogOut className="w-4 h-4" />
                    Sign out
                  </button>
                </div>
              )}
            </div>

            {/* Mobile hamburger */}
            <button
              id="mobile-menu-button"
              onClick={() => setMobileOpen(!mobileOpen)}
              className="md:hidden p-2 rounded-lg text-surface-400 hover:text-surface-200 hover:bg-surface-800/60 transition-all"
            >
              {mobileOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Nav */}
      {mobileOpen && (
        <div className="md:hidden border-t border-surface-700/40 animate-slide-down">
          <nav className="px-4 py-3 space-y-1">
            {navLinks.map((link) => (
              <Link
                key={link.to}
                to={link.to}
                onClick={() => setMobileOpen(false)}
                className={`
                  flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-all
                  ${isActive(link.to) ? 'bg-primary-500/10 text-primary-400' : 'text-surface-400 hover:text-surface-200 hover:bg-surface-800/60'}
                `}
              >
                {link.icon}
                {link.label}
              </Link>
            ))}
          </nav>
        </div>
      )}
    </header>
  );
}
