import os
import sys

from django.conf import settings
from django.http import Http404
from django.shortcuts import render
from django.template import Template
from django.utils._os import safe_join

ALLOWED_HOSTS = os.environ.get('ALLOWED_HOSTS').split(',')
BASE_DIR = os.path.dirname(__file__)

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
	TEMPLATES=(
		{
			 'BACKEND': 'django.template.backends.django.DjangoTemplates',
			 'DIRS': [],
			 'APP_DIRS': True,
		},
	),
	STATIC_URL='/static/',
	SITE_PAGES_DIRECTORY=os.path.join(BASE_DIR, 'pages'),
	SITE_OUTPUT_DIRECTORY=os.path.join(BASE_DIR, '_build'),
	STATIC_ROOT=os.path.join(BASE_DIR, '_build', 'static'),
)

if __name__ == "__main__":
	from django.core.management import execute_from_command_line
	
	execute_from_command_line(sys.argv)

