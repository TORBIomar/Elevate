import { useState, useEffect, type FormEvent } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { userService } from '../../services/userService';
import type { UserResponse } from '../../types';
import Input from '../../components/ui/Input';
import Button from '../../components/ui/Button';
import LoadingSpinner from '../../components/ui/LoadingSpinner';
import {
  User as UserIcon,
  Mail,
  Phone,
  Shield,
  Save,
  Camera,
  Calendar,
} from 'lucide-react';

export default function ProfilePage() {
  const { user: authUser } = useAuth();
  const [profile, setProfile] = useState<UserResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    phoneNumber: '',
    profilePictureUrl: '',
  });

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await userService.getProfile();
        setProfile(data);
        setForm({
          firstName: data.firstName || '',
          lastName: data.lastName || '',
          phoneNumber: data.phoneNumber || '',
          profilePictureUrl: data.profilePictureUrl || '',
        });
      } catch (err) {
        console.error('Failed to fetch profile:', err);
        setError('Failed to load your profile.');
      } finally {
        setIsLoading(false);
      }
    };
    fetchProfile();
  }, []);

  const update = (field: string) => (e: React.ChangeEvent<HTMLInputElement>) =>
    setForm((prev) => ({ ...prev, [field]: e.target.value }));

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setIsSaving(true);
    try {
      const updated = await userService.updateProfile({
        firstName: form.firstName,
        lastName: form.lastName,
        phoneNumber: form.phoneNumber || undefined,
        profilePictureUrl: form.profilePictureUrl || undefined,
      });
      setProfile(updated);
      setSuccess('Profile updated successfully!');
      setIsEditing(false);
    } catch {
      setError('Failed to update profile.');
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading) return <LoadingSpinner />;

  const initials = authUser
    ? `${authUser.firstName[0]}${authUser.lastName[0]}`.toUpperCase()
    : '?';

  return (
    <div className="mx-auto max-w-3xl px-4 sm:px-6 lg:px-8 py-8 animate-fade-in">
      <h1 className="text-3xl font-bold text-surface-100 mb-8">My Profile</h1>

      {/* Profile header card */}
      <div className="glass rounded-2xl p-6 sm:p-8 mb-6">
        <div className="flex flex-col sm:flex-row items-center gap-6">
          <div className="relative">
            <div className="w-24 h-24 rounded-2xl bg-gradient-to-br from-primary-500 to-accent-500 flex items-center justify-center text-white text-3xl font-bold shadow-2xl shadow-primary-500/20">
              {initials}
            </div>
            <div className="absolute -bottom-1 -right-1 w-8 h-8 rounded-lg bg-surface-800 border border-surface-700 flex items-center justify-center">
              <Camera className="w-4 h-4 text-surface-400" />
            </div>
          </div>
          <div className="text-center sm:text-left">
            <h2 className="text-2xl font-bold text-surface-100">
              {profile?.firstName} {profile?.lastName}
            </h2>
            <p className="text-surface-400 mt-1">{profile?.email}</p>
            <div className="flex items-center gap-2 mt-2 justify-center sm:justify-start">
              <span className="inline-flex items-center gap-1.5 text-xs font-semibold px-2.5 py-1 rounded-full bg-primary-500/10 text-primary-400 border border-primary-500/20">
                <Shield className="w-3 h-3" />
                {profile?.role}
              </span>
              {profile?.isActive && (
                <span className="inline-flex items-center gap-1 text-xs font-semibold px-2.5 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  Active
                </span>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Notifications */}
      {success && (
        <div className="mb-6 rounded-xl bg-emerald-500/10 border border-emerald-500/20 px-4 py-3 text-sm text-emerald-400 animate-slide-down">
          {success}
        </div>
      )}
      {error && (
        <div className="mb-6 rounded-xl bg-red-500/10 border border-red-500/20 px-4 py-3 text-sm text-red-400 animate-slide-down">
          {error}
        </div>
      )}

      {/* Profile details / edit form */}
      <div className="glass rounded-2xl p-6 sm:p-8">
        <div className="flex items-center justify-between mb-6">
          <h3 className="text-lg font-bold text-surface-100">Account Details</h3>
          {!isEditing && (
            <Button
              variant="secondary"
              size="sm"
              onClick={() => setIsEditing(true)}
            >
              Edit
            </Button>
          )}
        </div>

        {isEditing ? (
          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <Input
                label="First Name"
                value={form.firstName}
                onChange={update('firstName')}
                icon={<UserIcon className="w-4 h-4" />}
                required
              />
              <Input
                label="Last Name"
                value={form.lastName}
                onChange={update('lastName')}
                required
              />
            </div>
            <Input
              label="Phone Number"
              type="tel"
              value={form.phoneNumber}
              onChange={update('phoneNumber')}
              icon={<Phone className="w-4 h-4" />}
              placeholder="+212 6XX-XXXXXX"
            />
            <Input
              label="Profile Picture URL"
              type="url"
              value={form.profilePictureUrl}
              onChange={update('profilePictureUrl')}
              icon={<Camera className="w-4 h-4" />}
              placeholder="https://..."
            />

            <div className="flex justify-end gap-3 pt-4 border-t border-surface-700/30">
              <Button
                type="button"
                variant="secondary"
                onClick={() => setIsEditing(false)}
              >
                Cancel
              </Button>
              <Button
                type="submit"
                isLoading={isSaving}
                icon={<Save className="w-4 h-4" />}
              >
                Save Changes
              </Button>
            </div>
          </form>
        ) : (
          <div className="space-y-4">
            {[
              { icon: <UserIcon className="w-4 h-4" />, label: 'Full Name', value: `${profile?.firstName} ${profile?.lastName}` },
              { icon: <Mail className="w-4 h-4" />, label: 'Email', value: profile?.email },
              { icon: <Phone className="w-4 h-4" />, label: 'Phone', value: profile?.phoneNumber || 'Not provided' },
              { icon: <Shield className="w-4 h-4" />, label: 'Role', value: profile?.role },
              { icon: <Calendar className="w-4 h-4" />, label: 'Member since', value: profile?.createdAt ? new Date(profile.createdAt).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' }) : '-' },
            ].map((item) => (
              <div key={item.label} className="flex items-center gap-4 py-3 border-b border-surface-800/40 last:border-0">
                <div className="text-primary-400">{item.icon}</div>
                <div>
                  <p className="text-xs text-surface-500">{item.label}</p>
                  <p className="text-sm text-surface-200 font-medium">{item.value}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
