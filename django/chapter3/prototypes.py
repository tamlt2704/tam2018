import os
import sys

from django.conf import settings
from django.http import Http404
from django.shortcuts import render
from django.template import Template
from django.utils._os import safe_join

ALLOWED_HOSTS = os.environ.get('ALLOWED_HOSTS').split(',')
BASEDIR = os.path.dirname(__file__)

settings.configure(
	DEBUG=True,
	ALLOWED_HOSTS=ALLOWED_HOSTS,
	ROOT_URLCONF='sitebuilder.urls',
	SECRET_KEY=os.urandom(32),
	MIDDLEWARE_CLASSES=(),
	INSTALLED_APPS=(
		'django.contrib.staticfiles',
		'sitebuilder',
	),
	STATIC_URL='/static/',
	SITE_PAGES_DIRECTORY=os.path.join(BASEDIR, 'pages'),
)

if __name__ == "__main__":
	from django.core.management import execute_from_command_line
	
	execute_from_command_line(sys.argv)
