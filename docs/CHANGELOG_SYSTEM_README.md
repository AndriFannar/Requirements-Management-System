# Automated Changelog System

This directory contains GitHub Actions workflows for automated changelog management throughout the development lifecycle.

## 🎯 System Overview

The changelog system follows this flow:

```
Feature Development → Dev → Test → Preprod → Production
      ↓                                ↓         ↓
  Individual                      Pre-release  Release
  Changelogs                       (v1.1.0-rc) (v1.1.0)
                                         ↓         ↓
                                   Aggregated  Archived
```

## 📋 Workflows

### 1. `changelog-create-template.yml`
**Trigger:** When a `feature/*` branch is created  
**Purpose:** Automatically creates a changelog template file for the feature

**What it does:**
- Detects new feature branch creation
- Creates `changelogs/feat-<feature-name>.md` with a pre-filled template
- Commits the template to the feature branch

**Example:**
```bash
git checkout -b feature/user-authentication
# Workflow automatically creates: changelogs/feat-user-authentication.md
```

### 2. `changelog-validate.yml`
**Trigger:** Pull requests to `dev`, `test`, `preprod`, or `main`  
**Purpose:** Ensures feature changelogs exist and are properly formatted

**What it validates:**
- ✅ Changelog file exists in `changelogs/feat-<feature-name>.md`
- ✅ Contains at least one section header (`### Added`, `### Changed`, etc.)
- ✅ Sections have content (at least one bullet point)

**Comments on PR:**
- ✅ Success: Confirms changelog is valid
- ❌ Failure: Explains what's missing with a template to fix it

### 3. `changelog-aggregate-prerelease.yml`
**Trigger:** Tags matching `v*-rc*` (e.g., `v1.1.0-rc`, `v1.2.0-rc2`)  
**Purpose:** Aggregates all feature changelogs into main `CHANGELOG.md` for pre-release

**What it does:**
1. Collects all `changelogs/feat-*.md` files
2. Organizes entries by category (Added, Changed, Fixed, etc.)
3. Adds feature attribution to each entry
4. Updates/creates root `CHANGELOG.md` with new version section
5. Commits changes to `preprod` branch
6. Creates GitHub pre-release with combined changelog

**Example:**
```bash
git tag v1.1.0-rc
git push origin v1.1.0-rc
# Workflow aggregates all feature changelogs → CHANGELOG.md
# Creates pre-release on GitHub
```

**Resulting CHANGELOG.md:**
```markdown
# Changelog

## v1.1.0-rc

### Added
- OAuth2 login with Google and GitHub *(feat-user-auth)*
- Project templates with 5 built-in options *(feat-templates)*

### Changed
- API now requires authentication *(feat-user-auth)*
```

### 4. `changelog-finalize-release.yml`
**Trigger:** Tags matching `v*` (but NOT `v*-rc*`) (e.g., `v1.1.0`, `v2.0.0`)  
**Purpose:** Finalizes changelog for production release and archives feature changelogs

**What it does:**
1. Updates `CHANGELOG.md`: removes `-rc` suffix (`v1.1.0-rc` → `v1.1.0`)
2. Archives all `changelogs/feat-*.md` → `changelogs/1.1.0/`
3. Commits changes to `main` branch
4. Creates GitHub release with finalized changelog

**Example:**
```bash
git tag v1.1.0
git push origin v1.1.0
# Workflow finalizes CHANGELOG.md
# Archives: changelogs/feat-*.md → changelogs/1.1.0/
# Creates production release on GitHub
```

**Directory structure after release:**
```
changelogs/
├── 1.1.0/
│   ├── feat-user-auth.md
│   └── feat-templates.md
├── 1.0.0/
│   └── feat-initial.md
└── feat-new-feature.md  (unreleased, still in development)
```

## 🚀 Usage Guide

### For Feature Development

1. **Create a feature branch:**
   ```bash
   git checkout -b feature/my-awesome-feature
   ```
   
2. **Edit the auto-generated changelog:**
   The workflow creates `changelogs/feat-my-awesome-feature.md` automatically.
   
   Update it as you develop:
   ```markdown
   ### Added
   - User can now export reports as PDF
   - Added email notifications for completed exports
   
   ### Changed
   - Export button moved to header for better visibility
   
   ### Technical Notes
   - New dependency: `pdfkit` for PDF generation
   - Database migration: `V005__add_export_settings.sql`
   ```

3. **Create a PR:**
   The validation workflow will check your changelog and comment on the PR.

### For Pre-releases (QA/Testing)

1. **Tag for pre-release on `preprod` branch:**
   ```bash
   git checkout preprod
   git tag v1.1.0-rc
   git push origin v1.1.0-rc
   ```

2. **Review the aggregated changelog:**
   - Check the updated `CHANGELOG.md` in preprod
   - Review the GitHub pre-release
   - Test thoroughly

3. **Make changes if needed:**
   - Update feature changelogs
   - Create new RC tag: `v1.1.0-rc2`

### For Production Release

1. **Tag for release on `main` branch:**
   ```bash
   git checkout main
   git tag v1.1.0
   git push origin v1.1.0
   ```

2. **Automatic finalization:**
   - Changelog is finalized (RC suffix removed)
   - Feature changelogs are archived
   - Production release is created on GitHub

## 📝 Changelog Format Guidelines

Follow [Keep a Changelog](https://keepachangelog.com/) format:

### Sections (use what applies)
- **Added**: New features
- **Changed**: Changes to existing functionality
- **Deprecated**: Soon-to-be removed features
- **Removed**: Removed features
- **Fixed**: Bug fixes
- **Security**: Security fixes
- **Technical Notes**: Migration steps, breaking changes, config updates

### Writing Tips
✅ **Good:**
- "Added OAuth2 authentication with Google provider"
- "Fixed memory leak in WebSocket connection handling"
- "Changed default timeout from 30s to 60s"

❌ **Avoid:**
- "Updated code" (too vague)
- "Fixed bug" (which bug?)
- "Refactored UserService" (too technical, not user-focused)

### Example Feature Changelog
```markdown
### Added
- User profile management page with avatar upload
- Email notifications for password changes
- Two-factor authentication with TOTP

### Changed
- Login page redesigned with improved mobile layout
- Password requirements now enforce minimum 12 characters

### Fixed
- Fixed session timeout not working correctly on mobile browsers

### Technical Notes
- Database migration: `V004__add_user_profiles.sql`
- New environment variables: `TOTP_SECRET_KEY`, `EMAIL_SMTP_HOST`
- Breaking change: `/api/users` endpoint now returns different JSON structure
```

## 🔧 Setup Requirements

### 1. Personal Access Token (PAT)

The workflows need a PAT to commit to protected branches.

**Create a fine-grained PAT:**
1. GitHub Settings → Developer settings → Personal access tokens → Fine-grained tokens
2. Generate new token with:
   - **Repository access**: Select your requirements repository
   - **Permissions**:
     - Contents: Read and write
     - Pull requests: Read and write

3. Add to repository secrets:
   - Name: `PAT_CHANGELOG`
   - Value: Your generated token

### 2. Branch Protection

Configure branch protection for `dev`, `test`, `preprod`, `main`:
- ✅ Require pull request reviews
- ✅ Require status checks to pass
- ✅ Require conversation resolution

The PAT allows workflows to bypass these rules for automated commits.

### 3. Workflow Files

Copy these workflow files to `.github/workflows/`:
- `changelog-create-template.yml`
- `changelog-validate.yml`
- `changelog-aggregate-prerelease.yml`
- `changelog-finalize-release.yml`

## 🎓 Learning Opportunities

This system teaches:
- **GitHub Actions:** Advanced workflows, matrix strategies, conditional logic
- **Git Operations:** Tagging, branch management, merge strategies
- **Release Management:** Semantic versioning, pre-releases, changelog best practices
- **Automation:** CI/CD pipelines, automated documentation
- **Shell Scripting:** Text processing, file manipulation, automation

## 🐛 Troubleshooting

### "Changelog validation failed" on PR
- Ensure `changelogs/feat-<feature-name>.md` exists
- Check that it has at least one section header with content
- Review the PR comment for specific guidance

### "Tag must be on preprod branch"
- Pre-release tags (`v*-rc*`) must be created on the `preprod` branch
- Production tags (`v*`) must be created on the `main` branch

### Workflow doesn't trigger
- Ensure workflow files are in `.github/workflows/`
- Check that PAT is correctly configured in repository secrets
- Verify tag naming matches patterns (e.g., `v1.1.0-rc`, not `1.1.0-rc`)

### Permission denied when pushing
- Verify `PAT_CHANGELOG` secret exists and is valid
- Ensure PAT has correct permissions (Contents: write)
- Check that token hasn't expired

## 📚 Additional Resources

- [Keep a Changelog](https://keepachangelog.com/)
- [Semantic Versioning](https://semver.org/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [GitHub Releases](https://docs.github.com/en/repositories/releasing-projects-on-github)

## 🔄 Version History

This changelog system is itself versioned and improved over time. See the main project changelog for updates to the automation workflows.
