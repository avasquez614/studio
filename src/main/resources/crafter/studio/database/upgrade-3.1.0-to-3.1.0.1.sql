UPDATE `group`
SET group_name = 'system_admin', group_description = 'System Administrator group'
WHERE group_name = 'crafter-admin' ;

UPDATE `group`
SET group_name = 'site_admin', group_description = 'Site Administrator group'
WHERE group_name = 'Admin' ;

UPDATE `group`
SET group_name = 'site_author', group_description = 'Site Author group'
WHERE group_name = 'Author' ;

UPDATE `group`
SET group_name = 'site_publisher', group_description = 'Site Publisher group'
WHERE group_name = 'Publisher' ;

UPDATE `group`
SET group_name = 'site_developer', group_description = 'Site Developer group'
WHERE group_name = 'Developer' ;

UPDATE `group`
SET group_name = 'site_reviewer', group_description = 'Site Reviewer group'
WHERE group_name = 'Reviewer' ;

UPDATE _meta SET version = '3.1.0.1' ;