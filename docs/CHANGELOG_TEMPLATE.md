# Feature Changelog Template

> Copy this template when creating a new feature changelog manually, or let the `changelog-create-template.yml` workflow create it automatically when you create a feature branch.

---

<!-- 
Changelog for feature: feat-<feature-name>
Author: <your-github-username>
Created: <YYYY-MM-DD>
Target version: TBD (will be determined when feature is scheduled for release)
-->

## Feature: [Brief Feature Title]

### Added
- New feature or capability added
- Another new feature
- API endpoints, UI components, etc.

### Changed
- Existing functionality that was modified
- Behavior changes users will notice
- Performance improvements

### Fixed
- Bug fixes included in this feature
- Issues resolved

### Removed
- Deprecated features or functionality removed
- Endpoints or APIs no longer available

### Deprecated
- Features marked for future removal
- Migration guidance for deprecated features

### Security
- Security-related changes
- Vulnerability fixes

### Technical Notes
- **Database migrations:** `V001__migration_name.sql`, `V002__another_migration.sql`
- **New dependencies:** `spring-boot-starter-oauth2-client`, `jsonwebtoken:0.12.5`
- **Configuration changes:** 
  - New environment variables: `OAUTH_CLIENT_ID`, `OAUTH_CLIENT_SECRET`
  - Updated application.yml: `spring.security.oauth2.client.*`
- **Breaking changes:**
  - API endpoint `/api/users` now requires authentication
  - Response format changed from flat structure to nested
- **Migration guide:**
  - Step 1: Update environment variables
  - Step 2: Run database migrations
  - Step 3: Update client applications to use new authentication

---

## Guidelines for Writing Good Changelogs

### Be User-Focused
Think about what users (or other developers) need to know:
- ✅ "Added OAuth2 login with Google and GitHub providers"
- ❌ "Implemented OAuth2Service class with JWT token generation"

### Be Specific
Provide enough detail to understand the change:
- ✅ "Fixed memory leak in WebSocket connection causing 100MB/hour growth"
- ❌ "Fixed bug in connections"

### Be Concise
One line per change, get to the point:
- ✅ "Increased API timeout from 30s to 60s for large exports"
- ❌ "We noticed that some users were experiencing timeouts when exporting large datasets, so after investigation we decided to increase the timeout setting in the configuration from 30 seconds to 60 seconds to give more time for the export process to complete."

### Use Action Verbs
Start with clear action words:
- Added, Fixed, Changed, Removed, Deprecated, Updated, Improved, etc.

### Include Context When Needed
For breaking changes or migrations, explain what users need to do:
- "Changed authentication from API keys to JWT tokens. Existing API keys will work until v2.0.0. See migration guide for upgrading."

### Remove Unused Sections
Don't leave empty sections in your final changelog:
- If you didn't add security fixes, remove `### Security`
- If there are no breaking changes, remove that part of Technical Notes

---

## Examples from Real Projects

### Example 1: Authentication Feature
```markdown
### Added
- OAuth2 authentication with Google, GitHub, and Microsoft providers
- JWT token-based session management with automatic refresh
- User profile management page with avatar upload
- Two-factor authentication using TOTP (Time-based One-Time Password)

### Changed
- All API endpoints now require authentication by default
- Login page redesigned with social login buttons
- Session timeout increased from 1 hour to 24 hours for "Remember me"

### Security
- Passwords now hashed with bcrypt (cost factor: 12)
- Implemented rate limiting on login endpoint (5 attempts per minute)
- Added CSRF protection for all state-changing requests

### Technical Notes
- **Database migrations:** `V003__create_users.sql`, `V004__add_oauth_providers.sql`
- **New dependencies:** 
  - `spring-boot-starter-oauth2-client:3.2.0`
  - `jjwt:0.12.5`
  - `bcrypt:0.10.2`
- **Environment variables:**
  - `OAUTH_GOOGLE_CLIENT_ID` and `OAUTH_GOOGLE_CLIENT_SECRET`
  - `OAUTH_GITHUB_CLIENT_ID` and `OAUTH_GITHUB_CLIENT_SECRET`
  - `JWT_SECRET` (generate with: `openssl rand -base64 32`)
- **Breaking changes:**
  - All API endpoints now return 401 Unauthorized without valid JWT token
  - Previous session-based authentication is deprecated (removed in v2.0.0)
```

### Example 2: Database Performance Improvement
```markdown
### Changed
- Optimized project listing query, reducing load time from 3s to 200ms
- Database indexes added for frequently queried columns

### Fixed
- Fixed N+1 query problem in use case retrieval (reduced queries from 100+ to 2)
- Corrected transaction timeout causing failures on large batch operations

### Technical Notes
- **Database migrations:** `V007__add_performance_indexes.sql`
- **Performance impact:** 
  - Average API response time improved by 85%
  - Database CPU usage reduced by 60%
- **No breaking changes**
```

### Example 3: UI Enhancement
```markdown
### Added
- Dark mode support with automatic theme switching based on system preference
- Keyboard shortcuts for common actions (see documentation for full list)
- Responsive design for tablets and mobile devices

### Changed
- Sidebar navigation redesigned for better space utilization
- Project cards now show last modified date and author
- Color scheme updated to meet WCAG AA accessibility standards

### Fixed
- Fixed text overflow in long project names
- Corrected z-index issues causing modals to appear behind sidebar

### Technical Notes
- **New dependencies:** `@radix-ui/react-dropdown-menu`, `tailwindcss-dark-mode`
- **No breaking changes**
- **No database migrations required**
```

---

## Common Mistakes to Avoid

### ❌ Too Technical
```markdown
### Changed
- Refactored UserService to use repository pattern with generic DAO implementation
```
**Better:**
```markdown
### Changed
- Improved user data loading performance by 50%
```

### ❌ Too Vague
```markdown
### Fixed
- Fixed issues
- Updated code
- Improved things
```
**Better:**
```markdown
### Fixed
- Fixed session timeout not working on Safari browser
- Fixed duplicate email notifications being sent
- Fixed memory leak in WebSocket connections
```

### ❌ Too Much Detail
```markdown
### Added
- Added a new React component called ProjectCard that takes props including 
  projectId, title, description, author, and lastModified. The component uses 
  Tailwind CSS for styling and implements onClick handlers...
```
**Better:**
```markdown
### Added
- Project card component with click-to-open functionality
```

---

## Keep a Changelog Format Reference

This template follows [Keep a Changelog](https://keepachangelog.com/) 1.1.0 format.

**Standard sections** (in order):
1. Added - for new features
2. Changed - for changes in existing functionality
3. Deprecated - for soon-to-be removed features
4. Removed - for now removed features
5. Fixed - for any bug fixes
6. Security - in case of vulnerabilities

**Custom section:**
- Technical Notes - for implementation details, migrations, breaking changes

---

**Need help?** Check the [CHANGELOG_SYSTEM_README.md](./CHANGELOG_SYSTEM_README.md) for the full documentation on the automated changelog system.
